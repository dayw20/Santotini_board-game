// Fixed StartScreen.tsx - No Extra Padding
import React, { useState } from 'react';
import { motion } from 'framer-motion';

interface StartScreenProps {
  onStartGame: (playerA: string, playerB: string) => void;
}

const StartScreen: React.FC<StartScreenProps> = ({ onStartGame }) => {
  const [playerA, setPlayerA] = useState('');
  const [playerB, setPlayerB] = useState('');
  const [showIntro, setShowIntro] = useState(true);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (playerA.trim() && playerB.trim()) {
      onStartGame(playerA.trim(), playerB.trim());
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center fixed inset-0 m-0 p-0">
      {/* This removes the background from the page itself, making it fully white */}
      <div className="fixed inset-0 bg-white"></div>
      
      {/* Center card with gradient background */}
      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.8, type: "spring" }}
        className="w-full max-w-md backdrop-blur-lg p-8 rounded-3xl shadow-2xl overflow-hidden relative z-10"
        style={{ 
          background: "linear-gradient(135deg, #f0f7ff 0%, #e6f0fb 100%)",
          boxShadow: "0 8px 32px rgba(31, 38, 135, 0.2)",
          border: "1px solid rgba(255, 255, 255, 0.3)"
        }}
      >
        {/* Background elements */}
        <div className="absolute -top-10 -right-10 w-32 h-32 bg-blue-200 rounded-full opacity-60 blur-md"></div>
        <div className="absolute -bottom-8 -left-8 w-24 h-24 bg-indigo-200 rounded-full opacity-60 blur-md"></div>
        
        {/* Game title */}
        <motion.div 
          className="text-center mb-8"
          initial={{ y: -50 }}
          animate={{ y: 0 }}
          transition={{ delay: 0.2, type: "spring" }}
        >
          <h1 className="text-5xl font-extrabold text-transparent bg-clip-text bg-gradient-to-br from-blue-600 to-purple-700 tracking-wide drop-shadow-sm mb-2">
            Santorini
          </h1>
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.5 }}
          >
            <span className="text-4xl" role="img" aria-label="temple">🏛️</span>
          </motion.div>
        </motion.div>
        
        {/* Introduction / Game description */}
        {showIntro && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="mb-6 text-center text-gray-700"
          >
            <p>
              Build your way to victory in this abstract strategy game set in the beautiful islands of Santorini.
            </p>
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="mt-4 px-4 py-2 bg-gradient-to-r from-blue-500 to-indigo-500 text-white rounded-full shadow-md"
              onClick={() => setShowIntro(false)}
            >
              Start Playing
            </motion.button>
          </motion.div>
        )}
        
        {/* Player form */}
        {!showIntro && (
          <motion.form 
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.2 }}
            onSubmit={handleSubmit} 
            className="space-y-6"
          >
            <div className="space-y-6">
              <motion.div
                initial={{ x: -20, opacity: 0 }}
                animate={{ x: 0, opacity: 1 }}
                transition={{ delay: 0.3 }}
              >
                <label htmlFor="playerA" className="block text-gray-700 font-medium mb-2 text-lg">
                  <span className="inline-block w-6 h-6 rounded-full bg-blue-500 text-white text-center mr-2">A</span>
                  Blue Player:
                </label>
                <input
                  id="playerA"
                  type="text"
                  value={playerA}
                  onChange={(e) => setPlayerA(e.target.value)}
                  placeholder="Enter Blue Player's name"
                  className="w-full px-5 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-400 focus:outline-none shadow-inner text-lg bg-white bg-opacity-70"
                  required
                />
              </motion.div>
              
              <motion.div
                initial={{ x: 20, opacity: 0 }}
                animate={{ x: 0, opacity: 1 }}
                transition={{ delay: 0.4 }}
              >
                <label htmlFor="playerB" className="block text-gray-700 font-medium mb-2 text-lg">
                  <span className="inline-block w-6 h-6 rounded-full bg-red-500 text-white text-center mr-2">B</span>
                  Red Player:
                </label>
                <input
                  id="playerB"
                  type="text"
                  value={playerB}
                  onChange={(e) => setPlayerB(e.target.value)}
                  placeholder="Enter Red Player's name"
                  className="w-full px-5 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-400 focus:outline-none shadow-inner text-lg bg-white bg-opacity-70"
                  required
                />
              </motion.div>
            </div>
            
            <motion.button
              type="submit"
              whileHover={{ scale: 1.03 }}
              whileTap={{ scale: 0.97 }}
              className="w-full bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white font-semibold py-3 px-6 rounded-xl shadow-lg transition duration-300 ease-in-out transform"
            >
              Start Game
            </motion.button>
            
            <motion.button
              type="button"
              initial={{ opacity: 0 }}
              animate={{ opacity: 0.8 }}
              transition={{ delay: 0.6 }}
              className="w-full text-center text-gray-500 hover:text-gray-700 mt-4 text-sm"
              onClick={() => setShowIntro(true)}
            >
              ← Back to intro
            </motion.button>
          </motion.form>
        )}
        
        {/* Footer */}
        <div className="mt-8 text-center">
          <p className="text-sm text-gray-500">
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-indigo-600 font-medium">Santorini</span> - The divine strategy game
          </p>
        </div>
      </motion.div>
    </div>
  );
};

export default StartScreen;