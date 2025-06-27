package edu.eci.arsw.concurrent_matrix;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Entity class.
 */
class EntityTest {

    @Test
    void testEntityCreation() {
        Position pos = new Position(3, 4);
        Entity entity = new Entity(EntityType.AGENT, pos);
        
        assertEquals(EntityType.AGENT, entity.getType());
        assertEquals(pos, entity.getPosition());
    }

    @Test
    void testSetPosition() {
        Position initialPos = new Position(1, 1);
        Position newPos = new Position(2, 2);
        Entity entity = new Entity(EntityType.ENEMY, initialPos);
        
        assertEquals(initialPos, entity.getPosition());
        
        entity.setPosition(newPos);
        assertEquals(newPos, entity.getPosition());
    }

    @Test
    void testToString() {
        Position pos = new Position(5, 7);
        Entity agentEntity = new Entity(EntityType.AGENT, pos);
        Entity enemyEntity = new Entity(EntityType.ENEMY, pos);
        
        assertEquals("A at (5, 7)", agentEntity.toString());
        assertEquals("B at (5, 7)", enemyEntity.toString());
    }

    @Test
    void testEntityTypes() {
        Position pos = new Position(0, 0);
        
        Entity emptyEntity = new Entity(EntityType.EMPTY, pos);
        Entity obstacleEntity = new Entity(EntityType.OBSTACLE, pos);
        Entity phoneEntity = new Entity(EntityType.PHONE, pos);
        Entity agentEntity = new Entity(EntityType.AGENT, pos);
        Entity enemyEntity = new Entity(EntityType.ENEMY, pos);
        
        assertEquals(EntityType.EMPTY, emptyEntity.getType());
        assertEquals(EntityType.OBSTACLE, obstacleEntity.getType());
        assertEquals(EntityType.PHONE, phoneEntity.getType());
        assertEquals(EntityType.AGENT, agentEntity.getType());
        assertEquals(EntityType.ENEMY, enemyEntity.getType());
    }
}
