package edu.eci.arsw.concurrent_matrix;

import java.util.List;

/**
 * Thread representing the agent (A) that tries to reach a phone (T).
 * The agent uses A* pathfinding to navigate towards the nearest phone.
 */
public class AgentThread extends Thread {
    private final Board board;
    private final GameState gameState;
    private final int moveDelay;

    /**
     * Creates a new AgentThread.
     * 
     * @param board the game board
     * @param gameState the shared game state
     * @param moveDelay delay between moves in milliseconds
     */
    public AgentThread(Board board, GameState gameState, int moveDelay) {
        this.board = board;
        this.gameState = gameState;
        this.moveDelay = moveDelay;
        setName("Agent-Thread");
    }

    @Override
    public void run() {
        try {
            while (!gameState.isGameOver()) {
                Position currentPosition = board.getAgentPosition();
                if (currentPosition == null) {
                    gameState.setGameOver(true, "Agent not found on board");
                    break;
                }

                List<Position> phonePositions = board.getPhonePositions();
                if (phonePositions.isEmpty()) {
                    gameState.setGameOver(true, "No phones available");
                    break;
                }

                // Check if agent reached a phone
                EntityType currentEntity = board.getEntity(currentPosition);
                if (currentEntity == EntityType.PHONE) {
                    gameState.setGameOver(true, "Agent reached a phone! Agent wins!");
                    break;
                }

                // Check if agent has any path to phones
                if (!GameUtils.hasPathToAnyGoal(board, currentPosition, phonePositions)) {
                    gameState.setGameOver(true, "Agent has no path to any phone. Game over!");
                    break;
                }

                // Find next move towards nearest phone
                Position nextMove = GameUtils.findNextMoveTowards(board, currentPosition, phonePositions);
                
                if (nextMove != null) {
                    EntityType targetEntity = board.getEntity(nextMove);
                    
                    // Check if moving into an enemy (caught by enemy)
                    if (targetEntity == EntityType.ENEMY) {
                        gameState.setGameOver(true, "Agent was caught by an enemy! Enemies win!");
                        break;
                    }

                    // Move the agent
                    if (board.moveEntity(currentPosition, nextMove)) {
                        // Check if agent reached a phone after moving
                        if (targetEntity == EntityType.PHONE) {
                            gameState.setGameOver(true, "Agent reached a phone! Agent wins!");
                            break;
                        }
                    }
                } else {
                    // No valid move found
                    gameState.setGameOver(true, "Agent cannot move. Game over!");
                    break;
                }

                Thread.sleep(moveDelay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Agent thread was interrupted");
        } catch (Exception e) {
            System.err.println("Error in Agent thread: " + e.getMessage());
            e.printStackTrace();
            gameState.setGameOver(true, "Agent thread error: " + e.getMessage());
        }
    }
}
