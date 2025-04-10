// File: src/types/gameTypes.ts
export interface CellState {
    x: number;
    y: number;
    level: number;
    occupancy: string; // "EMPTY", "WORKER", "DOME"
    player?: string;
    workerIndex?: number;
  }
  
  export interface GameState {
    currentPlayer: string;
    winner: string | null;
    phase: string;
    gameOver: boolean;
    board: CellState[];
    players: string[]; 
  }
  
  export interface PlaceWorkerRequest {
    playerName: string;
    workerIndex: number;
    x: number;
    y: number;
  }
  
  export interface MoveRequest {
    playerName: string;
    workerIndex: number;
    targetX: number;
    targetY: number;
  }
  
  export interface BuildRequest {
    playerName: string;
    workerIndex: number;
    targetX: number;
    targetY: number;
  }
  
  export interface StartGameRequest {
    playerAName: string;
    playerBName: string;
  }
  
  export interface WorkerSelection {
    player: string;
    index: number;
  }

  export interface WorkerSelectionRequest {
  playerName: string;
  workerIndex: number;
}
