package org.game;

import edu.eci.arsw.concurrent_matrix.Game;

/**
 * Alternative main class to run the Concurrent Matrix Game.
 * This provides compatibility with the original project specification.
 */
public class Main {
    
    /**
     * Main method to start the Concurrent Matrix Game.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Starting Concurrent Matrix Game...");
        Game game = new Game();
        game.run();
    }
}
