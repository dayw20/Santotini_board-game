// Enhanced App.tsx
import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import GameBoard from './components/GameBoard';
import GameControls from './components/GameControls';
import GameStatus from './components/GameStatus';
import StartScreen from './components/StartScreen';
import GodSelectionScreen from './components/GodSelectionScreen';
import { fetchGameState, startGame, placeWorker, moveWorker, buildTower, selectWorker } from './api/gameApi';
import toast from 'react-hot-toast';
import axios from 'axios';
import GameOverModal from './components/GameOverModal';
import { GameState, WorkerSelection, CellState } from './types/gameTypes';




const getFriendlyErrorMessage = (serverMessage: string): string => {
  const errorMap: { [key: string]: string } = {
    "Invalid move": "Invalid move! You can only move to an adjacent empty cell.",
    "Invalid build action": "Invalid build! You can only build next to your worker.",
    "Cannot place worker on occupied cell": "That cell is already occupied.",
    "Player not found": "Player not found. Please restart the game.",
    "Not your turn or player not found": "It's not your turn!",
    "Invalid worker selection": "Please select one of your own workers.",
    "Invalid worker index": "Invalid worker index. Please restart the game.",
    "Cannot select worker at this phase": "You cannot select a worker right now.",
  };
  if (serverMessage.startsWith("Invalid move: target is not adjacent")) {
    return "You can only move to an adjacent cell.";
  }
  if (serverMessage.startsWith("Invalid move: target is occupied or domed")) {
    return "That space is blocked!";
  }
  if (serverMessage.startsWith("Invalid move: height too high")) {
    return "You can't climb more than 1 level!";
  }

  if (serverMessage.includes("Invalid move: push off board")) {
    return "You can't push the opponent — they’d fall off the board!";
  }
  if (serverMessage.includes("Invalid move: cannot push into occupied cell")) {
    return "You can't push the opponent — the space behind them is blocked.";
  }
  

  return errorMap[serverMessage] || serverMessage;
};


const App: React.FC = () => {
  const [gameState, setGameState] = useState<GameState | null>(null);
  const [selectedWorker, setSelectedWorker] = useState<WorkerSelection | null>(null);
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [isGameStarted, setIsGameStarted] = useState<boolean>(false);
  const [showRules, setShowRules] = useState<boolean>(false);
  const [pendingPlayers, setPendingPlayers] = useState<{ playerA: string; playerB: string } | null>(null);

  const isValidBuildLocation = (cell: CellState): boolean => {
    if (!selectedWorker || !gameState) return false;

    if (cell.occupancy !== 'EMPTY') return false;
  
    const workerCell = gameState.board.find(c =>
      c.occupancy === 'WORKER' &&
      c.player === selectedWorker.player &&
      c.workerIndex === selectedWorker.index
    );
    if (!workerCell) return false;
  
    const dx = Math.abs(cell.x - workerCell.x);
    const dy = Math.abs(cell.y - workerCell.y);
    const isAdjacent = dx <= 1 && dy <= 1 && !(dx === 0 && dy === 0);
  
    return isAdjacent;
  };
  
  // Fetch current game state
  const refreshGameState = async () => {
    try {
      const state = await fetchGameState();
      setGameState(state);

      if (selectedWorker && state.currentPlayer !== undefined && state.currentPlayer !== selectedWorker.player) {
        setSelectedWorker(null);
      }

      setErrorMessage('');
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage('Failed to fetch game state: ' + error.message);
      } else {
        setErrorMessage('Failed to fetch game state');
      }
    }
  };

  // Poll for game state updates
  useEffect(() => {
    if (isGameStarted) {
      const interval = setInterval(refreshGameState, 1000);
      return () => clearInterval(interval);
    }
  }, [isGameStarted]);

  // Initialize game with player names
  const handleStartGame = async (playerA: string, playerB: string, godA: string, godB: string) => {
    try {
      await startGame(playerA, playerB, godA, godB);
      setIsGameStarted(true);
      refreshGameState();
      setErrorMessage('');
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage('Failed to start game: ' + error.message);
      } else {
        setErrorMessage('Failed to start game');
      }
    }
  };

  // Handle clicking on a cell
  const handleCellClick = async (x: number, y: number) => {
    if (!gameState) return;
    
    try {
      console.log("Current phase:", gameState.phase, "Clicked at:", x, y);
      
      switch (gameState.phase) {
        case 'placingWorker':
          const placedWorkersCount = gameState.board.filter(
            cell => cell.occupancy === 'WORKER' && cell.player === gameState.currentPlayer
          ).length;
          await placeWorker(gameState.currentPlayer, placedWorkersCount, x, y);
          break;
          
          case 'selectingWorker':
            const clickedCell = gameState.board.find(cell => cell.x === x && cell.y === y);
          
            if (
              clickedCell &&
              clickedCell.occupancy === 'WORKER' &&
              clickedCell.player === gameState.currentPlayer &&
              clickedCell.workerIndex !== undefined
            ) {
              const playerWorkers = gameState.board.filter(cell => cell.player === gameState.currentPlayer && cell.occupancy === 'WORKER');
          
              if (clickedCell.workerIndex >= playerWorkers.length) {
                toast.error('Please select a valid worker.');
                return;
              }
          
              setSelectedWorker({
                player: gameState.currentPlayer,
                index: clickedCell.workerIndex
              });
          
              await selectWorker(gameState.currentPlayer, clickedCell.workerIndex);
            } else {
              toast.error('Please select one of your own workers.');
            }
            break;
          
        case 'moving':
          if (selectedWorker) {
            console.log("Moving worker:", selectedWorker.player, selectedWorker.index, "to", x, y);
            await moveWorker(selectedWorker.player, selectedWorker.index, x, y);
          } else {
            toast.error('Please select a worker first');
          }
          break;
          
        case 'building':
          if (selectedWorker) {
            console.log("Building with worker:", selectedWorker.player, selectedWorker.index, "at", x, y);
            await buildTower(selectedWorker.player, selectedWorker.index, x, y);
            // setSelectedWorker(null);
          } else {
            toast.error('Please select a worker first');
          }
          break;
        
        case 'optionalAction': {
          const clickedCell = gameState.board.find(c => c.x === x && c.y === y);
            
          // Reuse isValidBuildLocation to check if user is trying to build
          if (selectedWorker && clickedCell && isValidBuildLocation(clickedCell)) {
            await buildTower(selectedWorker.player, selectedWorker.index, x, y);
            setSelectedWorker(null);
          } else {
              await axios.post('/api/game/pass'); // user clicked irrelevant cell
            }
            break;
          }
      }
      
      // Refresh the game state after any action
      await refreshGameState();
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.data) {
        const data = error.response.data;
    
        let message: string = '';
    
        if (typeof data === 'string') {
          message = data;
        } else if (typeof data === 'object' && 'message' in data) {
          message = (data as any).message;
        } else {
          message = JSON.stringify(data);
        }
    
        console.log("🚨 Backend error message:", message); 
        const friendly = getFriendlyErrorMessage(message);
        toast.error(friendly);
      } else if (error instanceof Error) {
        toast.error(error.message);
      } else {
        toast.error('An unexpected error occurred');
      }
      console.error("Error:", error);
    }
  };

  // Start a new game
  const handleNewGame = () => {
    setIsGameStarted(false);
    setPendingPlayers(null);
    setSelectedWorker(null);
    setGameState(null);
    setErrorMessage('');
  };

  if (!isGameStarted) {
    if (!pendingPlayers) {
      return <StartScreen onContinue={(a, b) => setPendingPlayers({ playerA: a, playerB: b })} />;
    } else {
      return <GodSelectionScreen playerA={pendingPlayers.playerA} playerB={pendingPlayers.playerB} onStartGame={handleStartGame} />;
    }
  }


  if (!gameState) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-100 to-white flex items-center justify-center">
  
        <motion.div
          animate={{ rotate: 360 }}
          transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
          className="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full"
        ></motion.div>
        <div className="ml-4 text-xl font-medium text-blue-800">Loading game...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-100 to-sky-50 px-4 py-8">
      {gameState.gameOver && gameState.winner && (
        <GameOverModal winner={gameState.winner} onNewGame={handleNewGame} />
      )}
      <div className="flex-grow flex flex-col items-center">


        <motion.div 
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.8 }}
          className="max-w-4xl mx-auto"
        >
          {/* Game header */}
          <motion.div
            initial={{ y: -50 }}
            animate={{ y: 0 }}
            className="text-center mb-8"
          >
            <h1 className="text-5xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-blue-500 mb-2">
              Santorini
            </h1>
            <p className="text-gray-600 italic">The divine strategy game</p>
          </motion.div>
          
          {/* Error message */}
          <AnimatePresence>
            {errorMessage && (
              <motion.div 
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0 }}
                transition={{ duration: 0.3 }}
                className="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded mb-6 shadow-md"
              >
                <div className="flex items-center">
                  <svg className="w-6 h-6 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                  </svg>
                  {errorMessage}
                </div>
              </motion.div>
            )}
          </AnimatePresence>
          
          {/* Game status */}
          <GameStatus gameState={gameState} />
          <div className="flex bg-white bg-opacity-70 backdrop-blur-sm rounded-xl shadow-xl p-6 mb-6">
          {/* Game Board */}
            <div className="w-[500px]">
              <GameBoard 
                  gameState={gameState} 
                  selectedWorker={selectedWorker} 
                  onCellClick={handleCellClick} 
                />
            </div>
            <div className="ml-4 w-40 text-sm">
            <div className="p-3 rounded-xl bg-white bg-opacity-80 backdrop-blur-md shadow-md text-center text-sm">
                <h2 className="text-lg font-semibold text-indigo-700 mb-3">Emoji Legend</h2>
                <div className="grid grid-cols-1 gap-2 text-gray-700 text-sm">
                  <div className="flex items-center gap-2">
                    <span className="text-lg">👷 👷‍♂️</span> Player A Workers
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-lg">👨‍🔧 👩‍🔧</span> Player B Workers
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-lg">🏠 🏘️ 🏙️</span> Tower Levels 1-3
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-lg">🏛️</span> Dome
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-lg">🟢</span> Valid Move
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-lg">🔵</span> Valid Build
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          {/* Game controls */}
          <div className="flex justify-between items-center">
            <button 
              onClick={() => setShowRules(!showRules)}
              className="bg-indigo-100 hover:bg-indigo-200 text-indigo-800 px-4 py-2 rounded-lg font-medium transition shadow-sm flex items-center"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              {showRules ? 'Hide Rules' : 'Show Rules'}
            </button>
            
            <GameControls 
              onNewGame={handleNewGame} 
            />
          </div>
          
          {/* Rules panel */}
          <AnimatePresence>
            {showRules && (
              <motion.div
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: 'auto' }}
                exit={{ opacity: 0, height: 0 }}
                transition={{ duration: 0.3 }}
                className="mt-6 bg-indigo-50 p-6 rounded-xl shadow-inner overflow-hidden"
              >
                <h3 className="font-bold text-lg mb-3 text-indigo-900">Game Rules:</h3>
                <ul className="space-y-2 text-indigo-800">
                  <li className="flex items-start">
                    <span className="mr-2 text-lg">🏃</span>
                    <span>Players take turns moving one worker and then building with that worker</span>
                  </li>
                  <li className="flex items-start">
                    <span className="mr-2 text-lg">⬆️</span>
                    <span>Workers can move one space in any direction and climb up one level</span>
                  </li>
                  <li className="flex items-start">
                    <span className="mr-2 text-lg">🏗️</span>
                    <span>After moving, the worker must build one level on an adjacent space</span>
                  </li>
                  <li className="flex items-start">
                    <span className="mr-2 text-lg">🏆</span>
                    <span>The first player to move a worker to level 3 wins the game</span>
                  </li>
                </ul>
              </motion.div>
            )}
          </AnimatePresence>
          {gameState.phase === 'optionalAction' && (
            <button
              onClick={() => axios.post('/api/game/pass')}
              className="mt-6 bg-yellow-500 text-white px-6 py-2 rounded-lg shadow-md hover:bg-yellow-600"
            >
              Skip Second Build
            </button>
          )}
        </motion.div>
        </div>
    </div>
  );
};

export default App;