package com.yvette.santorini.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yvette.santorini.backend.service.GameService;
import com.yvette.santorini.backend.dto.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*") 
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * Start a new game with the specified players
     */
    @PostMapping("/start")
    public ResponseEntity<Void> startGame(@RequestBody StartGameRequest request) {
        gameService.startGame(request.getPlayerAName(), request.getPlayerBName());
        return ResponseEntity.ok().build();
    }

    /**
     * Get the current game state
     */
    @GetMapping("/state")
    public ResponseEntity<GameStateResponse> getGameState() {
        return ResponseEntity.ok(gameService.getGameState());
    }

    /**
     * Place a worker on the board
     */
    @PostMapping("/place-worker")
    public ResponseEntity<?> placeWorker(@RequestBody PlaceWorkerRequest request) {
        try {
            boolean success = gameService.placeWorker(request);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Cannot place worker on occupied cell");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /**
     * Select a worker for the current player
     */
    @PostMapping("/select-worker")
    public ResponseEntity<?> selectWorker(@RequestBody WorkerSelectionRequest request) {
        try {
            boolean success = gameService.selectWorker(request);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Invalid worker selection");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Move a worker on the board
     */
    @PostMapping("/move")
    public ResponseEntity<?> moveWorker(@RequestBody MoveRequest request) {
        try {
            boolean success = gameService.moveWorker(request);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Invalid move");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Build with a worker on the board
     */
    @PostMapping("/build")
    public ResponseEntity<?> buildTower(@RequestBody BuildRequest request) {
        try {
            boolean success = gameService.buildTower(request);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Invalid build action");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}