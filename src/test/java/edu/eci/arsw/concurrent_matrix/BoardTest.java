package edu.eci.arsw.concurrent_matrix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for the Board class.
 */
class BoardTest {
    
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testBoardInitialization() {
        // Test that board is initialized with empty spaces
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Position pos = new Position(i, j);
                assertEquals(EntityType.EMPTY, board.getEntity(pos));
            }
        }
    }

    @Test
    void testSetAndGetEntity() {
        Position pos = new Position(5, 5);
        board.setEntity(pos, EntityType.AGENT);
        assertEquals(EntityType.AGENT, board.getEntity(pos));
        
        board.setEntity(pos, EntityType.PHONE);
        assertEquals(EntityType.PHONE, board.getEntity(pos));
    }

    @Test
    void testIsValidPosition() {
        assertTrue(board.isValidPosition(new Position(0, 0)));
        assertTrue(board.isValidPosition(new Position(9, 9)));
        assertTrue(board.isValidPosition(new Position(5, 5)));
        
        assertFalse(board.isValidPosition(new Position(-1, 0)));
        assertFalse(board.isValidPosition(new Position(0, -1)));
        assertFalse(board.isValidPosition(new Position(10, 5)));
        assertFalse(board.isValidPosition(new Position(5, 10)));
    }

    @Test
    void testIsFree() {
        Position pos = new Position(3, 3);
        assertTrue(board.isFree(pos));
        
        board.setEntity(pos, EntityType.OBSTACLE);
        assertFalse(board.isFree(pos));
        
        board.setEntity(pos, EntityType.EMPTY);
        assertTrue(board.isFree(pos));
    }

    @Test
    void testMoveEntity() {
        Position from = new Position(2, 2);
        Position to = new Position(3, 3);
        
        // Place an agent
        board.setEntity(from, EntityType.AGENT);
        
        // Move the agent
        assertTrue(board.moveEntity(from, to));
        assertEquals(EntityType.EMPTY, board.getEntity(from));
        assertEquals(EntityType.AGENT, board.getEntity(to));
    }

    @Test
    void testMoveEntityToOccupiedSpace() {
        Position from = new Position(2, 2);
        Position to = new Position(3, 3);
        
        // Place entities
        board.setEntity(from, EntityType.AGENT);
        board.setEntity(to, EntityType.OBSTACLE);
        
        // Try to move to occupied space
        assertFalse(board.moveEntity(from, to));
        assertEquals(EntityType.AGENT, board.getEntity(from));
        assertEquals(EntityType.OBSTACLE, board.getEntity(to));
    }

    @Test
    void testMoveEntityToPhone() {
        Position from = new Position(2, 2);
        Position to = new Position(3, 3);
        
        // Place agent and phone
        board.setEntity(from, EntityType.AGENT);
        board.setEntity(to, EntityType.PHONE);
        
        // Move agent to phone (should be allowed)
        assertTrue(board.moveEntity(from, to));
        assertEquals(EntityType.EMPTY, board.getEntity(from));
        assertEquals(EntityType.AGENT, board.getEntity(to));
    }

    @Test
    void testGetAdjacentPositions() {
        Position center = new Position(5, 5);
        List<Position> adjacent = board.getAdjacentPositions(center);
        
        assertEquals(4, adjacent.size());
        assertTrue(adjacent.contains(new Position(4, 5)));
        assertTrue(adjacent.contains(new Position(6, 5)));
        assertTrue(adjacent.contains(new Position(5, 4)));
        assertTrue(adjacent.contains(new Position(5, 6)));
    }

    @Test
    void testGetAdjacentPositionsAtEdge() {
        Position corner = new Position(0, 0);
        List<Position> adjacent = board.getAdjacentPositions(corner);
        
        assertEquals(2, adjacent.size());
        assertTrue(adjacent.contains(new Position(1, 0)));
        assertTrue(adjacent.contains(new Position(0, 1)));
    }

    @Test
    void testSetupGame() {
        board.setupGame();
        
        // Check that phones, agent, and enemies are placed
        assertNotNull(board.getAgentPosition());
        assertFalse(board.getPhonePositions().isEmpty());
        assertFalse(board.getEnemyPositions().isEmpty());
        
        // Check that agent position is valid
        Position agentPos = board.getAgentPosition();
        assertTrue(board.isValidPosition(agentPos));
        assertEquals(EntityType.AGENT, board.getEntity(agentPos));
    }

    @Test
    void testGetOutOfBoundsPosition() {
        Position outOfBounds = new Position(-1, -1);
        // Out of bounds should be treated as obstacle
        assertEquals(EntityType.OBSTACLE, board.getEntity(outOfBounds));
    }
}
