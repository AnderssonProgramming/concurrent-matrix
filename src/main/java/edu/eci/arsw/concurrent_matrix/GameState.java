package edu.eci.arsw.concurrent_matrix;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe class to manage the overall game state.
 * Tracks whether the game is over and the reason for game end.
 */
public class GameState {
    private boolean gameOver;
    private String endReason;
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Creates a new GameState with game not over.
     */
    public GameState() {
        this.gameOver = false;
        this.endReason = "";
    }

    /**
     * Checks if the game is over (thread-safe).
     * 
     * @return true if the game is over
     */
    public boolean isGameOver() {
        lock.lock();
        try {
            return gameOver;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the game as over with a reason (thread-safe).
     * 
     * @param gameOver whether the game is over
     * @param reason the reason for game ending
     */
    public void setGameOver(boolean gameOver, String reason) {
        lock.lock();
        try {
            if (!this.gameOver) { // Only set if not already over
                this.gameOver = gameOver;
                this.endReason = reason;
                if (gameOver) {
                    System.out.println("GAME OVER: " + reason);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the reason for game ending (thread-safe).
     * 
     * @return the end reason
     */
    public String getEndReason() {
        lock.lock();
        try {
            return endReason;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Resets the game state for a new game.
     */
    public void reset() {
        lock.lock();
        try {
            this.gameOver = false;
            this.endReason = "";
        } finally {
            lock.unlock();
        }
    }
}
