package edu.eci.arsw.concurrent_matrix;

import java.util.Objects;

/**
 * Represents a position in the 2D matrix with x and y coordinates.
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Creates a new Position with the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of this position.
     * 
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of this position.
     * 
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Calculates the Manhattan distance to another position.
     * 
     * @param other the other position
     * @return the Manhattan distance
     */
    public int distanceTo(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
