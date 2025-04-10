import React from 'react';

interface GameOverModalProps {
  winner: string;
  onNewGame: () => void;
}

const GameOverModal: React.FC<GameOverModalProps> = ({ winner, onNewGame }) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-xl shadow-xl p-8 max-w-sm w-full text-center">
        <h2 className="text-2xl font-bold text-green-600 mb-4">🎉 Game Over!</h2>
        <p className="text-lg mb-6">{winner} wins the game! 🏆</p>
        <button
          onClick={onNewGame}
          className="bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded-lg shadow transition"
        >
          Play Again
        </button>
      </div>
    </div>
  );
};

export default GameOverModal;
