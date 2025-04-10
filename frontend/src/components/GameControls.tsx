// File: src/components/GameControls.tsx
import React from 'react';

interface GameControlsProps {
  onNewGame: () => void;
}

const GameControls: React.FC<GameControlsProps> = ({onNewGame}) => {
  return (
    <div className="flex justify-center gap-4 mt-4">
     
        <button 
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          onClick={onNewGame}
        >
          New Game
        </button>
  
    </div>
  );
};

export default GameControls;