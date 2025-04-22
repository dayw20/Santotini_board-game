import React, { useState } from 'react';
import { motion } from 'framer-motion';

interface GodSelectionScreenProps {
  playerA: string;
  playerB: string;
  onStartGame: (playerA: string, playerB: string, godA: string, godB: string) => void;
}

const availableGods = [
  { name: 'none', label: 'None 🙅', description: 'Play without a god power.' },
  { name: 'demeter', label: 'Demeter 🌾', description: 'Build twice, not on the same cell.' },
  { name: 'minotaur', label: 'Minotaur 🐂', description: 'Push opponent worker if cell behind is free.' },
  { name: 'hephaestus', label: 'Hephaestus 🔨', description: 'Optionally build twice on the same space (block only).' },
  { name: 'pan', label: 'Pan 🐐', description: 'Also win if Worker moves down two or more levels.' },
  { name: 'atlas', label: 'Atlas 🗿', description: 'May build a dome at any level.' },
  { name: 'apollo', label: 'Apollo 🏛️', description: 'Swap with opponent worker when moving.' }, // ✅ Add this
];



const GodSelectionScreen: React.FC<GodSelectionScreenProps> = ({ playerA, playerB, onStartGame }) => {
  const [godA, setGodA] = useState('none');
  const [godB, setGodB] = useState('none');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onStartGame(playerA, playerB, godA, godB);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 to-white px-4">
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-xl"
      >
        <h2 className="text-3xl font-bold text-center text-indigo-700 mb-6">Choose Your God Powers</h2>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block font-semibold text-indigo-600 mb-1">{playerA}'s God</label>
            <select
              className="w-full border px-4 py-2 rounded-lg shadow focus:outline-none focus:ring-2 focus:ring-indigo-400"
              value={godA}
              onChange={e => setGodA(e.target.value)}
            >
              {availableGods.map(god => (
                <option key={god.name} value={god.name}>{god.label}</option>
              ))}
            </select>
            <p className="text-sm text-gray-600 mt-1">
              {availableGods.find(g => g.name === godA)?.description}
            </p>
          </div>

          <div>
            <label className="block font-semibold text-red-600 mb-1">{playerB}'s God</label>
            <select
              className="w-full border px-4 py-2 rounded-lg shadow focus:outline-none focus:ring-2 focus:ring-red-400"
              value={godB}
              onChange={e => setGodB(e.target.value)}
            >
              {availableGods.map(god => (
                <option key={god.name} value={god.name}>{god.label}</option>
              ))}
            </select>
            <p className="text-sm text-gray-600 mt-1">
              {availableGods.find(g => g.name === godB)?.description}
            </p>
          </div>

          <button
            type="submit"
            className="w-full mt-4 bg-indigo-500 hover:bg-indigo-600 text-white font-semibold py-3 px-6 rounded-xl shadow-lg transition duration-300 ease-in-out"
          >
            Start Game
          </button>
        </form>
      </motion.div>
    </div>
  );
};

export default GodSelectionScreen;
