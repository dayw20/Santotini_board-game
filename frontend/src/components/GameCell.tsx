import React from 'react';
import { CellState } from '../types/gameTypes';

interface GameCellProps {
  cell: CellState;
  isValidMove?: boolean;
  isValidBuild?: boolean;
  isSelectedWorker?: boolean;
  onCellClick: (x: number, y: number) => void;
  playerNames: (string | undefined)[]; 
}


const GameCell: React.FC<GameCellProps> = ({ 
  cell, 
  isValidMove, 
  isValidBuild, 
  isSelectedWorker,
  onCellClick ,
  playerNames
}) => {
  // New tower symbols using emoji
  const towerSymbols = ['', '🏠', '🏘️', '🏙️'];
  const playerAName = playerNames[0];
  const playerBName = playerNames[1];

  const getCellContent = () => {
    if (cell.occupancy === 'WORKER') {
      // Worker symbols 
      const playerAWorkers = ['👷', '👷‍♂️'];
      const playerBWorkers = ['👨‍🔧', '👩‍🔧'];
      
      let workerEmoji;
      
      if (cell.player === playerAName) {
        workerEmoji = playerAWorkers[cell.workerIndex || 0];
      } else if (cell.player === playerBName) {
        workerEmoji = playerBWorkers[cell.workerIndex || 0];
      } else {
        workerEmoji = '❓'; 
      }

      // Return worker with level indicator if on a building
      if (cell.level > 0) {
        return (
          <div className="flex flex-col items-center justify-center">
            <div>{workerEmoji}</div>
            <div className="text-xs font-bold">{getLevelIndicator(cell.level)}</div>
          </div>
        );
      }
      
      return workerEmoji;
    } else if (cell.occupancy === 'DOME') {
      // Dome symbol
      return '🏛️';
    } else {
      // Empty cell or just a building
      return towerSymbols[cell.level];
    }
  };

  const getLevelIndicator = (level: number) => {
    switch (level) {
      case 1: return 'L₁';
      case 2: return 'L₂';
      case 3: return 'L₃';
      default: return '';
    }
  };

  const getBgColor = () => {
    if (isSelectedWorker) return 'bg-yellow-200 ring-4 ring-yellow-400 shadow-inner shadow-yellow-400';

    if (isValidMove) return 'bg-green-200 shadow-md shadow-green-400';
    if (isValidBuild) return 'bg-blue-200 shadow-md shadow-blue-400';
    if (cell.highlightType === "FIRST_BUILD") {
      return 'bg-yellow-300 shadow-yellow-500';
    }
    
    
    switch (cell.level) {
      case 1: return 'bg-gray-200';
      case 2: return 'bg-gray-300';
      case 3: return 'bg-gray-400';
      default: return (cell.x + cell.y) % 2 === 0 ? 'bg-board' : 'bg-board-dark';
    }
  };
  

  const getWorkerColor = () => {
    if (cell.occupancy !== 'WORKER') return '';
    return cell.player?.includes('A') ? 'text-player-a' : 'text-player-b';
  };

  return (
    <button
      className={`aspect-square w-full flex items-center justify-center border border-gray-300 rounded-lg shadow-sm hover:shadow-md transition duration-200 ease-in-out hover:scale-105 ${getBgColor()} ${getWorkerColor()} text-center text-xl md:text-3xl`}
      onClick={() => onCellClick(cell.x, cell.y)}
    >
      {getCellContent()}
    </button>
  );
};

export default GameCell;