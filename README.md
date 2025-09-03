# 🏛️ Santorini Board Game

A full-stack implementation of the classic **Santorini** board game, featuring an interactive web interface and sophisticated game logic with Greek god powers.

![Santorini Game](https://img.shields.io/badge/Game-Santorini-blue)
![Java](https://img.shields.io/badge/Backend-Java%2017+-orange)
![React](https://img.shields.io/badge/Frontend-React%2019%2B-blue)
![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-green)
![License](https://img.shields.io/badge/License-MIT-green)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-blue)

## 🎮 Game Overview

Santorini is a strategy board game where players take on the role of Greek gods, building towers and moving workers on a 5x5 grid. The goal is to reach the third level of a tower with one of your workers, or to prevent your opponent from making a valid move.

### ✨ Key Features

- **Full Game Logic**: Complete implementation of Santorini rules
- **Greek God Powers**: 6 unique god cards with special abilities
- **Interactive UI**: Modern React-based web interface
- **Real-time Gameplay**: Smooth turn-based gameplay with visual feedback
- **Responsive Design**: Works on desktop and mobile devices
- **Clean Architecture**: Separation of concerns with backend API and frontend UI

## 🏗️ Architecture

This project follows a clean, modern architecture with clear separation of concerns:

```
santorini/
├── backend/          # Java Spring Boot API
│   ├── controller/   # REST API endpoints
│   ├── service/      # Game business logic
│   ├── model/        # Game domain models
│   ├── dto/          # Data transfer objects
│   └── godpowers/    # God card implementations
└── frontend/         # React TypeScript UI
    ├── components/   # React components
    ├── api/          # API client
    └── types/        # TypeScript definitions
```

## 🚀 Getting Started

### Prerequisites

- **Java 17+** and **Maven** for backend
- **Node.js 18+** and **npm** for frontend

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will be available at `http://localhost:5173`

## 🎯 Game Rules

### Basic Gameplay
1. **Placement Phase**: Players place their workers on the board
2. **Movement Phase**: Move one worker to an adjacent cell (can climb up 1 level)
3. **Building Phase**: Build a tower level or dome on an adjacent cell
4. **Victory**: Reach level 3 of a tower, or block opponent's moves

### God Powers

The game includes 6 unique Greek god cards, each with special abilities:

- **Apollo**: Can move into opponent's space, pushing them away
- **Atlas**: Can build a dome at any level
- **Demeter**: Can build twice (not on the same space)
- **Hephaestus**: Can build up to 2 levels on the same space
- **Minotaur**: Can push opponent workers when moving into their space
- **Pan**: Wins by moving down 2 or more levels

## 🛠️ Technology Stack

### Backend
- **Java 17** - Core programming language
- **Spring Boot 3.4.4** - Web framework
- **Maven** - Build tool and dependency management
- **Lombok** - Reduces boilerplate code
- **Strategy Pattern** - For implementing god powers

### Frontend
- **React 19** - UI framework
- **TypeScript** - Type-safe JavaScript
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Utility-first CSS framework
- **Framer Motion** - Animation library
- **Axios** - HTTP client

## 🎨 Design Patterns

The project implements several design patterns and principles:

- **Strategy Pattern**: For different god power implementations
- **Factory Pattern**: For creating god card instances
- **MVC Architecture**: Clear separation of concerns
- **RESTful API**: Standard HTTP-based communication
- **State Management**: React hooks for game state

## 🧪 Testing

The backend includes comprehensive test coverage:

```bash
cd backend
./mvnw test
```

Tests cover:
- Game logic and rules
- God power implementations
- API endpoints
- Integration scenarios

## 📱 Features

### Game Interface
- Interactive 5x5 game board
- Visual feedback for valid moves and builds
- Turn indicators and game status
- Worker selection and movement
- Tower building with level indicators

### User Experience
- Smooth animations and transitions
- Responsive design for all devices
- Error handling with user-friendly messages
- Game state persistence
- Real-time game updates

## 🔧 API Endpoints

The backend provides a RESTful API:

- `POST /api/game/start` - Start a new game
- `GET /api/game/state` - Get current game state
- `POST /api/game/place-worker` - Place worker on board
- `POST /api/game/select-worker` - Select worker for action
- `POST /api/game/move` - Move selected worker
- `POST /api/game/build` - Build tower level or dome
- `POST /api/game/pass` - Pass build phase

## 🚀 Deployment

### Backend
```bash
cd backend
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
npm run build
# Serve the dist/ folder with your preferred web server

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📞 Support

If you have any questions or need help, please open an issue on GitHub.
```

