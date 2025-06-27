package edu.eci.arsw.concurrent_matrix;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Position class.
 */
class PositionTest {

    @Test
    void testPositionCreation() {
        Position pos = new Position(5, 3);
        assertEquals(5, pos.getX());
        assertEquals(3, pos.getY());
    }

    @Test
    void testEquals() {
        Position pos1 = new Position(2, 4);
        Position pos2 = new Position(2, 4);
        Position pos3 = new Position(3, 4);
        
        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
        assertNotEquals(null, pos1);
        assertNotEquals("not a position", pos1);
    }

    @Test
    void testHashCode() {
        Position pos1 = new Position(2, 4);
        Position pos2 = new Position(2, 4);
        Position pos3 = new Position(3, 4);
        
        assertEquals(pos1.hashCode(), pos2.hashCode());
        assertNotEquals(pos1.hashCode(), pos3.hashCode());
    }

    @Test
    void testDistanceTo() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3, 4);
        Position pos3 = new Position(1, 1);
        
        assertEquals(7, pos1.distanceTo(pos2)); // Manhattan distance: |0-3| + |0-4| = 7
        assertEquals(2, pos1.distanceTo(pos3)); // Manhattan distance: |0-1| + |0-1| = 2
        assertEquals(0, pos1.distanceTo(pos1)); // Distance to self is 0
    }

    @Test
    void testToString() {
        Position pos = new Position(5, 3);
        assertEquals("(5, 3)", pos.toString());
    }
}
