# Concurrent Matrix Game

MatrixConcurrent is a concurrent simulation game written in Java using Spring Boot. The game represents a 10x10 matrix-based battlefield where different types of entities interact in real-time using multiple threads.

## Game Entities

- **#** Obstacles: Block the path and cannot be moved through
- **T** Phones: Goal positions that the agent tries to reach
- **A** Agent: Tries to reach a phone using optimal pathfinding
- **B** Enemies: Try to catch the agent before it reaches a phone
- **` `** Empty space: Free positions where entities can move

## Game Rules

Each Agent (A) and Enemy (B) is controlled by its own Java thread, creating a truly concurrent simulation. The game ends when:

1. **Agent wins**: Agent reaches a phone (T)
2. **Enemies win**: An enemy (B) catches the agent (A)
3. **Game over**: Agent has no path to any phone

The board updates every 2 seconds in the terminal, showing the current state of all entities.

## Features

- **Thread-safe implementation**: Uses ReadWriteLocks for concurrent board access
- **A* pathfinding algorithm**: Intelligent movement for both agents and enemies
- **Real-time visualization**: Board updates every 2 seconds
- **Dynamic entity placement**: Random placement of obstacles, phones, agents, and enemies
- **Comprehensive testing**: Full unit test suite with JUnit 5

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Git (optional, for cloning)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/AnderssonProgramming/concurrent-matrix.git
cd concurrent-matrix
```

2. Build the project using Maven:
```bash
mvn clean install
```

### Running the Application

You can run the simulation in several ways:

#### Using Maven Spring Boot Plugin:
```bash
mvn spring-boot:run
```

#### Using Maven Package:
```bash
mvn package
java -jar target/concurrent-matrix-0.0.1-SNAPSHOT.jar
```

#### Running directly with Maven:
```bash
mvn clean compile exec:java -Dexec.mainClass="edu.eci.arsw.concurrent_matrix.Game"
```

The game will initialize the matrix, place the entities randomly, and start the concurrent simulation.

## Game Architecture

### Core Classes

- **`Game`**: Main game controller that manages the simulation
- **`Board`**: Thread-safe 10x10 matrix representing the game field
- **`Position`**: Immutable coordinate representation with utility methods
- **`Entity`**: Represents game entities with type and position
- **`EntityType`**: Enumeration of different entity types
- **`GameState`**: Thread-safe game state management
- **`GameUtils`**: Utility class with A* pathfinding implementation
- **`AgentThread`**: Thread controlling the agent's behavior
- **`EnemyThread`**: Thread controlling enemy behavior

### Concurrency Design

- **Thread Safety**: All shared resources use appropriate synchronization mechanisms
- **ReadWriteLock**: Used in Board class for efficient concurrent access
- **ReentrantLock**: Used in GameState for atomic state changes
- **Separate Threads**: Each agent and enemy runs in its own thread
- **Coordinated Termination**: Clean shutdown of all threads when game ends

## Algorithm Details

### A* Pathfinding

The game uses the A* algorithm for intelligent pathfinding:

- **Heuristic**: Manhattan distance to goal
- **Cost Function**: Actual movement cost from start
- **Path Reconstruction**: Traces back optimal path to determine next move
- **Obstacle Avoidance**: Dynamically avoids obstacles and other entities

### Movement Strategy

- **Agent**: Moves towards the nearest phone using optimal pathfinding
- **Enemies**: Move towards the agent's current position using A* algorithm
- **Collision Detection**: Checks for entity collisions before movement
- **Boundary Validation**: Ensures all movements stay within the 10x10 grid

## Running Tests

Run the comprehensive unit test suite:

```bash
mvn test
```

### Test Coverage

The project includes extensive unit tests covering:

- **Position calculations and comparisons**
- **Board operations and thread safety**
- **Pathfinding algorithm correctness**
- **Entity behavior and state management**
- **Game state management and concurrency**

### Example Test Cases

```java
@Test
public void testDirectPath_NoObstacles() {
    Board board = new Board();
    Position start = new Position(0, 0);
    Position goal = new Position(0, 2);
    board.setEntity(start, EntityType.AGENT);
    board.setEntity(goal, EntityType.PHONE);
    Position next = GameUtils.findNextMoveTowards(board, start, List.of(goal));
    assertEquals(new Position(0, 1), next);
}
```

## Game Configuration

### Timing Configuration

- **Display Interval**: 2000ms (2 seconds between board updates)
- **Move Interval**: 1000ms (1 second between entity moves)
- **Thread Timeout**: 3000ms for graceful thread termination

### Board Configuration

- **Size**: 10x10 matrix
- **Obstacles**: 15-20% of board coverage
- **Phones**: 2-3 phones randomly placed
- **Enemies**: 2-4 enemies randomly placed
- **Agent**: 1 agent randomly placed

## Deployment

### Building for Production

```bash
mvn clean package -DskipTests
```

### Running the JAR

```bash
java -jar target/concurrent-matrix-0.0.1-SNAPSHOT.jar
```

## Architecture Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   AgentThread   │    │  EnemyThread(s) │    │   GameState     │
│                 │    │                 │    │                 │
│ - Pathfinding   │    │ - Chase Agent   │    │ - Game Over     │
│ - Move to Phone │    │ - Collision Det │    │ - End Reason    │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────┼─────────────┐
                    │                           │
          ┌─────────▼───────┐        ┌─────────▼───────┐
          │      Board      │        │    GameUtils    │
          │                 │        │                 │
          │ - 10x10 Matrix  │        │ - A* Algorithm  │
          │ - Thread Safe   │        │ - Pathfinding   │
          │ - Entity Track  │        │ - Move Logic    │
          └─────────────────┘        └─────────────────┘
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- A* pathfinding algorithm implementation
- Java concurrency best practices
- Spring Boot framework for easy application setup