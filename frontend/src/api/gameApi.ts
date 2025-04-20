// File: src/api/gameApi.ts
import axios from 'axios';
import { 
  GameState, 
  StartGameRequest, 
  PlaceWorkerRequest, 
  MoveRequest, 
  BuildRequest 
} from '../types/gameTypes';

const API_URL = 'http://localhost:8080/api';

// Get current game state
export const fetchGameState = async (): Promise<GameState> => {
  try {
    const response = await axios.get<GameState>(`${API_URL}/game/state`);
    return response.data;
  } catch (error) {
    console.error('Error fetching game state:', error);
    throw error;
  }
};

// Start a new game with player names
export const startGame = async (playerA: string, playerB: string, godA: string, godB: string): Promise<void> => {
  try {
    const request: StartGameRequest = {
      playerAName: playerA,
      playerBName: playerB,
      playerAGod: godA,
      playerBGod: godB
    };
    await axios.post(`${API_URL}/game/start`, request);
  } catch (error) {
    console.error('Error starting game:', error);
    throw error;
  }
};


// Place a worker on the board
export const placeWorker = async (
  playerName: string, 
  workerIndex: number, 
  x: number, 
  y: number
): Promise<void> => {
  try {
    const request: PlaceWorkerRequest = { playerName, workerIndex, x, y };
    await axios.post(`${API_URL}/game/place-worker`, request);
  } catch (error) {
    console.error('Error placing worker:', error);
    throw error;
  }
};

export const selectWorker = async (
    playerName: string, 
    workerIndex: number
  ): Promise<void> => {
    try {
      const request = { playerName, workerIndex };
      await axios.post(`${API_URL}/game/select-worker`, request);
    } catch (error) {
      console.error('Error selecting worker:', error);
      throw error;
    }
  };

// Move a worker on the board
export const moveWorker = async (
  playerName: string, 
  workerIndex: number, 
  targetX: number, 
  targetY: number
): Promise<void> => {
  try {
    const request: MoveRequest = { playerName, workerIndex, targetX, targetY };
    await axios.post(`${API_URL}/game/move`, request);
  } catch (error) {
    console.error('Error moving worker:', error);
    throw error;
  }
};

// Build a tower on the board
export const buildTower = async (
  playerName: string, 
  workerIndex: number, 
  targetX: number, 
  targetY: number
): Promise<void> => {
  try {
    const request: BuildRequest = { playerName, workerIndex, targetX, targetY };
    await axios.post(`${API_URL}/game/build`, request);
  } catch (error) {
    console.error('Error building tower:', error);
    throw error;
  }
};