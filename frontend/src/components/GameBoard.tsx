
// File: src/components/GameBoard.tsx
import React from 'react';
import GameCell from './GameCell';
import { CellState, GameState, WorkerSelection } from '../types/gameTypes';

interface GameBoardProps {
  gameState: GameState;
  selectedWorker: WorkerSelection | null;
  onCellClick: (x: number, y: number) => void;
}

const GameBoard: React.FC<GameBoardProps> = ({ gameState, selectedWorker, onCellClick }) => {
  // Create a 5x5 grid from the board data
  const grid: CellState[][] = Array(5).fill(null).map(() => Array(5).fill(null));
  const playerNames = gameState.board
  .filter(cell => cell.occupancy === 'WORKER')
  .map(cell => cell.player)
  .filter((name, index, self) => name !== undefined && self.indexOf(name) === index);

  
  // Fill the grid with cell data
  gameState.board.forEach(cell => {
    grid[cell.x][cell.y] = cell;
  });

  // Determine if a cell is valid for moving or building
  const isValidMoveLocation = (cell: CellState): boolean => {
    if (!selectedWorker || gameState.phase !== 'moving') return false;
    if (cell.occupancy !== 'EMPTY') return false;
    
    // Find the currently selected worker's cell
    const workerCell = gameState.board.find(c => 
      c.occupancy === 'WORKER' && 
      c.player === selectedWorker.player && 
      c.workerIndex === selectedWorker.index
    );
    
    if (!workerCell) return false;
    
    // Check if adjacent
    const dx = Math.abs(cell.x - workerCell.x);
    const dy = Math.abs(cell.y - workerCell.y);
    const isAdjacent = dx <= 1 && dy <= 1 && !(dx === 0 && dy === 0);
    
    // Check level difference
    const canClimb = cell.level - workerCell.level <= 1;
    
    return isAdjacent && canClimb;
  };
  
  const isValidBuildLocation = (cell: CellState): boolean => {
    if (!selectedWorker || gameState.phase !== 'building') return false;
    if (cell.occupancy !== 'EMPTY') return false;
    
    // Find the currently selected worker's cell
    const workerCell = gameState.board.find(c => 
      c.occupancy === 'WORKER' && 
      c.player === selectedWorker.player && 
      c.workerIndex === selectedWorker.index
    );
    
    if (!workerCell) return false;
    
    // Check if adjacent
    const dx = Math.abs(cell.x - workerCell.x);
    const dy = Math.abs(cell.y - workerCell.y);
    const isAdjacent = dx <= 1 && dy <= 1 && !(dx === 0 && dy === 0);
    
    return isAdjacent;
  };
  
  const isSelectedWorker = (cell: CellState): boolean => {
    return (
      cell.occupancy === 'WORKER' && 
      selectedWorker !== null && 
      cell.player === selectedWorker.player && 
      cell.workerIndex === selectedWorker.index
    );
  };

  const handleCellClick = (x: number, y: number) => {
    if (gameState.gameOver) {
      return;
    }
    onCellClick(x, y);
  };
  

  return (
    <div className="grid grid-cols-5 gap-1 w-full max-w-[400px] mx-auto">
      {grid.map((row, x) => 
        row.map((cell, y) => (
          <GameCell
            key={`${x}-${y}`}
            cell={cell}
            isValidMove={isValidMoveLocation(cell)}
            isValidBuild={isValidBuildLocation(cell)}
            isSelectedWorker={isSelectedWorker(cell)}
            onCellClick={handleCellClick}
            playerNames={playerNames}
          />
        ))
      )}
    </div>
  );
};

export default GameBoard;
