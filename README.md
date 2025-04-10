# Santorini Board Game

This is an implementation of the game **Santorini**, with a separate backend and frontend architecture.

- **Backend**: Java (Spring Boot), contains the full game core logic.
- **Frontend**: React + TypeScript, provides an interactive web interface.

The project follows clean separation of concerns:  
- The backend only provides API and game logic.
- The frontend is only responsible for user interface.

---

## How to Start the Game

### Backend (Core Game Logic)

1. Make sure you have **Java 17+** and **Maven** installed.
2. Navigate to the backend project directory.
3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
