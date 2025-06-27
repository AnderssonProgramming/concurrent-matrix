package edu.eci.arsw.concurrent_matrix;

/**
 * Thread representing an enemy (B) that tries to catch the agent (A).
 * Each enemy moves independently using its own thread.
 */
public class EnemyThread extends Thread {
    private final Board board;
    private final GameState gameState;
    private final Position initialPosition;
    private final int moveDelay;
    private final int enemyId;

    /**
     * Creates a new EnemyThread.
     * 
     * @param board the game board
     * @param gameState the shared game state
     * @param initialPosition the initial position of this enemy
     * @param enemyId unique identifier for this enemy
     * @param moveDelay delay between moves in milliseconds
     */
    public EnemyThread(Board board, GameState gameState, Position initialPosition, int enemyId, int moveDelay) {
        this.board = board;
        this.gameState = gameState;
        this.initialPosition = initialPosition;
        this.enemyId = enemyId;
        this.moveDelay = moveDelay;
        setName("Enemy-" + enemyId + "-Thread");
    }

    @Override
    public void run() {
        Position currentPosition = initialPosition;
        
        try {
            while (!gameState.isGameOver()) {
                Position agentPosition = board.getAgentPosition();
                if (agentPosition == null) {
                    // Agent is gone, game should be over
                    break;
                }

                // Check if enemy is adjacent to agent (caught the agent)
                if (isAdjacent(currentPosition, agentPosition)) {
                    gameState.setGameOver(true, "Enemy " + enemyId + " caught the agent! Enemies win!");
                    break;
                }

                // Check if enemy is on the same position as agent
                if (currentPosition.equals(agentPosition)) {
                    gameState.setGameOver(true, "Enemy " + enemyId + " caught the agent! Enemies win!");
                    break;
                }

                // Find best move towards agent
                Position nextMove = GameUtils.findBestMoveTowardsAgent(board, currentPosition, agentPosition);
                
                if (nextMove != null) {
                    EntityType targetEntity = board.getEntity(nextMove);
                    
                    // Check if moving to agent's position
                    if (nextMove.equals(agentPosition)) {
                        gameState.setGameOver(true, "Enemy " + enemyId + " caught the agent! Enemies win!");
                        break;
                    }

                    // Move if target is empty or agent
                    if (targetEntity == EntityType.EMPTY || targetEntity == EntityType.AGENT) {
                        if (board.moveEntity(currentPosition, nextMove)) {
                            currentPosition = nextMove;
                            
                            // Double check if we caught the agent after moving
                            if (currentPosition.equals(board.getAgentPosition())) {
                                gameState.setGameOver(true, "Enemy " + enemyId + " caught the agent! Enemies win!");
                                break;
                            }
                        }
                    }
                }

                Thread.sleep(moveDelay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Enemy " + enemyId + " thread was interrupted");
        } catch (Exception e) {
            System.err.println("Error in Enemy " + enemyId + " thread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if two positions are adjacent (including diagonally).
     * 
     * @param pos1 first position
     * @param pos2 second position
     * @return true if positions are adjacent
     */
    private boolean isAdjacent(Position pos1, Position pos2) {
        int dx = Math.abs(pos1.getX() - pos2.getX());
        int dy = Math.abs(pos1.getY() - pos2.getY());
        return (dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0);
    }
}
