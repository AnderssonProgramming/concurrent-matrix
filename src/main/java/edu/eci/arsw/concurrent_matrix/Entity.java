package edu.eci.arsw.concurrent_matrix;

/**
 * Represents an entity in the game with its type and position.
 */
public class Entity {
    private final EntityType type;
    private Position position;

    /**
     * Creates a new Entity with the specified type and position.
     * 
     * @param type the type of the entity
     * @param position the initial position of the entity
     */
    public Entity(EntityType type, Position position) {
        this.type = type;
        this.position = position;
    }

    /**
     * Gets the type of this entity.
     * 
     * @return the entity type
     */
    public EntityType getType() {
        return type;
    }

    /**
     * Gets the current position of this entity.
     * 
     * @return the current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this entity.
     * 
     * @param position the new position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return type.getSymbol() + " at " + position;
    }
}
