// File: src/types/gameTypes.ts
export interface CellState {
    x: number;
    y: number;
    level: number;
    occupancy: string; // "EMPTY", "WORKER", "DOME"
    player?: string;
    workerIndex?: number;
    highlightType?: "FIRST_BUILD" | "NONE";
  }
  export type GamePhase = 
  | "placingWorker" 
  | "selectingWorker" 
  | "moving" 
  | "building" 
  | "optionalAction" 
  | "end";

  export interface GameState {
    currentPlayer: string;
    winner: string | null;
    phase: GamePhase;
    gameOver: boolean;
    board: CellState[];
    players: string[]; 
    playerGods?: Record<string, string>;
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
    buildDome?: boolean;
  }
  
  export interface StartGameRequest {
    playerAName: string;
    playerBName: string;
    playerAGod: string;
    playerBGod: string;
  }
  
  export interface WorkerSelection {
    player: string;
    index: number;
  }

  export interface WorkerSelectionRequest {
  playerName: string;
  workerIndex: number;
}


