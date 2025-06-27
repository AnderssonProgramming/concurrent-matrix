package edu.eci.arsw.concurrent_matrix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the GameUtils class.
 */
class GameUtilsTest {
    
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testDirectPath_NoObstacles() {
        Position start = new Position(0, 0);
        Position goal = new Position(0, 2);
        board.setEntity(start, EntityType.AGENT);
        board.setEntity(goal, EntityType.PHONE);
        
        Position next = GameUtils.findNextMoveTowards(board, start, Arrays.asList(goal));
        assertEquals(new Position(0, 1), next);
    }

    @Test
    void testPathWithObstacles() {
        Position start = new Position(0, 0);
        Position goal = new Position(2, 0);
        
        // Create a wall
        board.setEntity(new Position(1, 0), EntityType.OBSTACLE);
        board.setEntity(start, EntityType.AGENT);
        board.setEntity(goal, EntityType.PHONE);
        
        Position next = GameUtils.findNextMoveTowards(board, start, Arrays.asList(goal));
        // Should move around the obstacle
        assertNotNull(next);
        assertNotEquals(new Position(1, 0), next); // Should not move into obstacle
    }

    @Test
    void testNoPathAvailable() {
        Position start = new Position(1, 1);
        Position goal = new Position(3, 3);
        
        // Surround the start position with obstacles
        board.setEntity(new Position(0, 1), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 0), EntityType.OBSTACLE);
        board.setEntity(new Position(2, 1), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 2), EntityType.OBSTACLE);
        
        board.setEntity(start, EntityType.AGENT);
        board.setEntity(goal, EntityType.PHONE);
        
        Position next = GameUtils.findNextMoveTowards(board, start, Arrays.asList(goal));
        assertNull(next); // No path should be available
    }

    @Test
    void testFindClosestGoal() {
        Position start = new Position(5, 5);
        Position goal1 = new Position(7, 7); // Distance: 4
        Position goal2 = new Position(6, 6); // Distance: 2 (closer)
        Position goal3 = new Position(8, 8); // Distance: 6
        
        board.setEntity(start, EntityType.AGENT);
        board.setEntity(goal1, EntityType.PHONE);
        board.setEntity(goal2, EntityType.PHONE);
        board.setEntity(goal3, EntityType.PHONE);
        
        List<Position> goals = Arrays.asList(goal1, goal2, goal3);
        Position next = GameUtils.findNextMoveTowards(board, start, goals);
        
        // Should move towards the closest goal (goal2)
        assertNotNull(next);
        // The next move should be in the direction of goal2
        assertTrue(next.distanceTo(goal2) < start.distanceTo(goal2));
    }

    @Test
    void testHasPathToAnyGoal() {
        Position start = new Position(0, 0);
        Position goal1 = new Position(9, 9);
        Position goal2 = new Position(0, 2);
        
        board.setEntity(start, EntityType.AGENT);
        board.setEntity(goal1, EntityType.PHONE);
        board.setEntity(goal2, EntityType.PHONE);
        
        List<Position> goals = Arrays.asList(goal1, goal2);
        assertTrue(GameUtils.hasPathToAnyGoal(board, start, goals));
    }

    @Test
    void testHasPathToAnyGoal_NoPath() {
        Position start = new Position(1, 1);
        Position goal = new Position(3, 3);
        
        // Surround the start position with obstacles
        board.setEntity(new Position(0, 1), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 0), EntityType.OBSTACLE);
        board.setEntity(new Position(2, 1), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 2), EntityType.OBSTACLE);
        
        board.setEntity(start, EntityType.AGENT);
        board.setEntity(goal, EntityType.PHONE);
        
        List<Position> goals = Arrays.asList(goal);
        assertFalse(GameUtils.hasPathToAnyGoal(board, start, goals));
    }

    @Test
    void testFindBestMoveTowardsAgent() {
        Position enemyPos = new Position(0, 0);
        Position agentPos = new Position(2, 2);
        
        board.setEntity(enemyPos, EntityType.ENEMY);
        board.setEntity(agentPos, EntityType.AGENT);
        
        Position next = GameUtils.findBestMoveTowardsAgent(board, enemyPos, agentPos);
        assertNotNull(next);
        
        // The next position should be closer to the agent
        assertTrue(next.distanceTo(agentPos) < enemyPos.distanceTo(agentPos));
    }

    @Test
    void testFindBestMoveTowardsAgent_NullAgent() {
        Position enemyPos = new Position(0, 0);
        
        Position next = GameUtils.findBestMoveTowardsAgent(board, enemyPos, null);
        assertNull(next);
    }

    @Test
    void testAlreadyAtGoal() {
        Position start = new Position(2, 2);
        Position goal = new Position(2, 2); // Same position
        
        board.setEntity(start, EntityType.AGENT);
        
        Position next = GameUtils.findNextMoveTowards(board, start, Arrays.asList(goal));
        assertNull(next); // Already at goal
    }

    @Test
    void testEmptyGoalsList() {
        Position start = new Position(2, 2);
        board.setEntity(start, EntityType.AGENT);
        
        Position next = GameUtils.findNextMoveTowards(board, start, Arrays.asList());
        assertNull(next); // No goals provided
    }
}
