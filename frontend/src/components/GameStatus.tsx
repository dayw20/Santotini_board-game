import React from 'react';
import { GameState } from '../types/gameTypes';

interface GameStatusProps {
  gameState: GameState;
}

const GameStatus: React.FC<GameStatusProps> = ({ gameState }) => {
  const getStatusMessage = () => {
    if (gameState.gameOver && gameState.winner) {
      return `🏆 Game Over! ${gameState.winner} has won!`;
    }

    switch (gameState.phase) {
      case 'placingWorker':
        return `${gameState.currentPlayer}'s turn to place a worker 🏗️`;
      case 'selectingWorker':
        return `${gameState.currentPlayer}'s turn to select a worker 🚶‍♂️`;
      case 'moving':
        return `${gameState.currentPlayer}'s turn to move the selected worker 🏃‍♂️`;
      case 'building':
        return `${gameState.currentPlayer}'s turn to build with the selected worker 🏠`;
      default:
        return 'Game in progress...';
    }
  };

  return (
    <div className="mb-4 p-2 rounded-xl bg-white bg-opacity-70 backdrop-blur-md shadow-lg text-center">
      <h2 className="text-xl font-bold text-indigo-700 mb-2">Game Status</h2>
      <p className="text-lg text-gray-700">{getStatusMessage()}</p>
    </div>
  );
};

export default GameStatus;
