package edu.eci.arsw.concurrent_matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main game controller that manages the concurrent matrix simulation.
 * Coordinates the board, game state, and all entity threads.
 */
public class Game {
    private static final int DISPLAY_INTERVAL = 2000; // 2 seconds
    private static final int MOVE_INTERVAL = 1000; // 1 second between moves
    
    private final Board board;
    private final GameState gameState;
    private AgentThread agentThread;
    private final List<EnemyThread> enemyThreads;

    /**
     * Creates a new Game instance.
     */
    public Game() {
        this.board = new Board();
        this.gameState = new GameState();
        this.enemyThreads = new ArrayList<>();
    }

    /**
     * Starts and runs the game simulation.
     */
    public void run() {
        System.out.println("=".repeat(50));
        System.out.println("         CONCURRENT MATRIX GAME");
        System.out.println("=".repeat(50));
        System.out.println("Legend:");
        System.out.println("  A = Agent (tries to reach phone)");
        System.out.println("  B = Enemy (tries to catch agent)");
        System.out.println("  T = Phone (goal for agent)");
        System.out.println("  # = Obstacle");
        System.out.println("  " + EntityType.EMPTY.getSymbol() + " = Empty space");
        System.out.println("=".repeat(50));

        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            playGame();
            
            System.out.println("\nWould you like to play again? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            playAgain = input.equals("y") || input.equals("yes");
            
            if (playAgain) {
                resetGame();
            }
        }

        System.out.println("Thanks for playing Concurrent Matrix Game!");
        scanner.close();
    }

    /**
     * Plays a single game session.
     */
    private void playGame() {
        // Setup the game board
        System.out.println("\nSetting up the game...");
        board.setupGame();
        
        // Display initial board
        System.out.println("Initial Board:");
        board.display();
        
        // Start all threads
        startThreads();
        
        // Game loop - display board every 2 seconds
        try {
            while (!gameState.isGameOver()) {
                Thread.sleep(DISPLAY_INTERVAL);
                if (!gameState.isGameOver()) {
                    System.out.println("Current Board:");
                    board.display();
                    displayGameStats();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Game loop interrupted");
        }

        // Stop all threads
        stopThreads();
        
        // Display final result
        System.out.println("\nFinal Board:");
        board.display();
        System.out.println("Game Result: " + gameState.getEndReason());
    }

    /**
     * Starts all game threads (agent and enemies).
     */
    private void startThreads() {
        // Start agent thread
        agentThread = new AgentThread(board, gameState, MOVE_INTERVAL);
        agentThread.start();

        // Start enemy threads
        List<Position> enemyPositions = board.getEnemyPositions();
        for (int i = 0; i < enemyPositions.size(); i++) {
            EnemyThread enemyThread = new EnemyThread(board, gameState, 
                                                    enemyPositions.get(i), i + 1, MOVE_INTERVAL);
            enemyThreads.add(enemyThread);
            enemyThread.start();
        }

        System.out.println("Started " + (1 + enemyThreads.size()) + " threads (1 agent + " 
                         + enemyThreads.size() + " enemies)");
    }

    /**
     * Stops all game threads and waits for them to finish.
     */
    private void stopThreads() {
        try {
            // Interrupt and wait for agent thread
            if (agentThread != null) {
                agentThread.interrupt();
                agentThread.join(3000); // Wait up to 3 seconds
            }

            // Interrupt and wait for enemy threads
            for (EnemyThread enemyThread : enemyThreads) {
                enemyThread.interrupt();
                enemyThread.join(3000); // Wait up to 3 seconds
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread stopping interrupted");
        }
    }

    /**
     * Displays current game statistics.
     */
    private void displayGameStats() {
        Position agentPos = board.getAgentPosition();
        List<Position> phonePositions = board.getPhonePositions();
        List<Position> enemyPositions = board.getEnemyPositions();

        System.out.println("Game Stats:");
        System.out.println("  Agent position: " + (agentPos != null ? agentPos : "Not found"));
        System.out.println("  Phones: " + phonePositions.size() + " remaining");
        System.out.println("  Enemies: " + enemyPositions.size() + " active");
        
        if (agentPos != null && !phonePositions.isEmpty()) {
            Position nearestPhone = GameUtils.findNextMoveTowards(board, agentPos, phonePositions) != null ? 
                                   phonePositions.get(0) : null;
            if (nearestPhone != null) {
                System.out.println("  Distance to nearest phone: " + agentPos.distanceTo(nearestPhone));
            }
        }
    }

    /**
     * Resets the game for a new round.
     */
    private void resetGame() {
        gameState.reset();
        enemyThreads.clear();
        agentThread = null;
        System.out.println("\nGame reset. Starting new game...");
    }

    /**
     * Main method to start the game.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
