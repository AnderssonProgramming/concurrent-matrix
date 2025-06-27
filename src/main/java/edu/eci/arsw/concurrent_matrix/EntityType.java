package edu.eci.arsw.concurrent_matrix;

/**
 * Enumeration of different entity types in the game.
 */
public enum EntityType {
    /**
     * Empty space on the board.
     */
    EMPTY(' '),
    
    /**
     * Obstacle that blocks movement.
     */
    OBSTACLE('#'),
    
    /**
     * Phone/goal that agent A tries to reach.
     */
    PHONE('T'),
    
    /**
     * Agent that tries to reach a phone.
     */
    AGENT('A'),
    
    /**
     * Enemy that tries to catch the agent.
     */
    ENEMY('B');

    private final char symbol;

    /**
     * Creates an EntityType with the specified symbol.
     * 
     * @param symbol the character symbol representing this entity type
     */
    EntityType(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the character symbol for this entity type.
     * 
     * @return the character symbol
     */
    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
