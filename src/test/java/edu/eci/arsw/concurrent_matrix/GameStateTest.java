package edu.eci.arsw.concurrent_matrix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GameState class.
 */
class GameStateTest {
    
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
    }

    @Test
    void testInitialState() {
        assertFalse(gameState.isGameOver());
        assertEquals("", gameState.getEndReason());
    }

    @Test
    void testSetGameOver() {
        gameState.setGameOver(true, "Test reason");
        
        assertTrue(gameState.isGameOver());
        assertEquals("Test reason", gameState.getEndReason());
    }

    @Test
    void testSetGameOverMultipleTimes() {
        gameState.setGameOver(true, "First reason");
        gameState.setGameOver(true, "Second reason");
        
        assertTrue(gameState.isGameOver());
        // Should keep the first reason
        assertEquals("First reason", gameState.getEndReason());
    }

    @Test
    void testReset() {
        gameState.setGameOver(true, "Game ended");
        assertTrue(gameState.isGameOver());
        assertEquals("Game ended", gameState.getEndReason());
        
        gameState.reset();
        assertFalse(gameState.isGameOver());
        assertEquals("", gameState.getEndReason());
    }

    @Test
    void testThreadSafety() throws InterruptedException {
        // Test concurrent access to game state
        Thread thread1 = new Thread(() -> gameState.setGameOver(true, "Thread 1"));
        Thread thread2 = new Thread(() -> gameState.setGameOver(true, "Thread 2"));
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        assertTrue(gameState.isGameOver());
        // One of the reasons should be set (whichever thread reached first)
        assertTrue(gameState.getEndReason().equals("Thread 1") || 
                  gameState.getEndReason().equals("Thread 2"));
    }
}
