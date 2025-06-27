package edu.eci.arsw.concurrent_matrix;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents the game board - a 10x10 matrix where entities interact.
 * Thread-safe implementation using ReadWriteLock for concurrent access.
 */
public class Board {
    public static final int SIZE = 10;
    private final EntityType[][] grid;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Position> phonePositions;
    private Position agentPosition;
    private final List<Position> enemyPositions;

    /**
     * Creates a new empty board and initializes entity tracking lists.
     */
    public Board() {
        this.grid = new EntityType[SIZE][SIZE];
        this.phonePositions = new ArrayList<>();
        this.enemyPositions = new ArrayList<>();
        initialize();
    }

    /**
     * Initializes the board with empty spaces.
     */
    private void initialize() {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    grid[i][j] = EntityType.EMPTY;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Sets up the initial game state with random entity placement.
     */
    public void setupGame() {
        lock.writeLock().lock();
        try {
            Random random = new Random();
            Set<Position> occupiedPositions = new HashSet<>();

            // Clear previous state
            phonePositions.clear();
            enemyPositions.clear();
            agentPosition = null;
            initialize();

            // Place obstacles (15-20% of the board)
            int numObstacles = 15 + random.nextInt(6);
            for (int i = 0; i < numObstacles; i++) {
                Position pos = getRandomFreePosition(occupiedPositions, random);
                if (pos != null) {
                    setEntityUnsafe(pos, EntityType.OBSTACLE);
                    occupiedPositions.add(pos);
                }
            }

            // Place phones (2-3 phones)
            int numPhones = 2 + random.nextInt(2);
            for (int i = 0; i < numPhones; i++) {
                Position pos = getRandomFreePosition(occupiedPositions, random);
                if (pos != null) {
                    setEntityUnsafe(pos, EntityType.PHONE);
                    phonePositions.add(pos);
                    occupiedPositions.add(pos);
                }
            }

            // Place agent
            Position agentPos = getRandomFreePosition(occupiedPositions, random);
            if (agentPos != null) {
                setEntityUnsafe(agentPos, EntityType.AGENT);
                agentPosition = agentPos;
                occupiedPositions.add(agentPos);
            }

            // Place enemies (2-4 enemies)
            int numEnemies = 2 + random.nextInt(3);
            for (int i = 0; i < numEnemies; i++) {
                Position pos = getRandomFreePosition(occupiedPositions, random);
                if (pos != null) {
                    setEntityUnsafe(pos, EntityType.ENEMY);
                    enemyPositions.add(pos);
                    occupiedPositions.add(pos);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets a random free position on the board.
     * 
     * @param occupiedPositions set of already occupied positions
     * @param random random number generator
     * @return a free position or null if none available
     */
    private Position getRandomFreePosition(Set<Position> occupiedPositions, Random random) {
        List<Position> freePositions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Position pos = new Position(i, j);
                if (!occupiedPositions.contains(pos) && grid[i][j] == EntityType.EMPTY) {
                    freePositions.add(pos);
                }
            }
        }
        return freePositions.isEmpty() ? null : freePositions.get(random.nextInt(freePositions.size()));
    }

    /**
     * Sets an entity at the specified position without acquiring locks.
     * Should only be called when write lock is already held.
     * 
     * @param position the position to set
     * @param entityType the entity type to place
     */
    private void setEntityUnsafe(Position position, EntityType entityType) {
        if (isValidPosition(position)) {
            grid[position.getX()][position.getY()] = entityType;
        }
    }

    /**
     * Sets an entity at the specified position (thread-safe).
     * 
     * @param position the position to set
     * @param entityType the entity type to place
     */
    public void setEntity(Position position, EntityType entityType) {
        lock.writeLock().lock();
        try {
            setEntityUnsafe(position, entityType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets the entity type at the specified position (thread-safe).
     * 
     * @param position the position to check
     * @return the entity type at that position
     */
    public EntityType getEntity(Position position) {
        lock.readLock().lock();
        try {
            if (!isValidPosition(position)) {
                return EntityType.OBSTACLE; // Treat out-of-bounds as obstacles
            }
            return grid[position.getX()][position.getY()];
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Checks if a position is valid (within board bounds).
     * 
     * @param position the position to check
     * @return true if the position is valid
     */
    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < SIZE &&
               position.getY() >= 0 && position.getY() < SIZE;
    }

    /**
     * Checks if a position is free (empty space).
     * 
     * @param position the position to check
     * @return true if the position is free
     */
    public boolean isFree(Position position) {
        return getEntity(position) == EntityType.EMPTY;
    }

    /**
     * Moves an entity from one position to another (thread-safe).
     * 
     * @param from the source position
     * @param to the destination position
     * @return true if the move was successful
     */
    public boolean moveEntity(Position from, Position to) {
        lock.writeLock().lock();
        try {
            if (!isValidPosition(from) || !isValidPosition(to)) {
                return false;
            }

            EntityType entityType = grid[from.getX()][from.getY()];
            if (entityType == EntityType.EMPTY || entityType == EntityType.OBSTACLE || entityType == EntityType.PHONE) {
                return false;
            }

            EntityType targetEntity = grid[to.getX()][to.getY()];
            if (targetEntity != EntityType.EMPTY && targetEntity != EntityType.PHONE) {
                return false; // Can't move to occupied space (except phones)
            }

            // Update entity positions in tracking lists
            if (entityType == EntityType.AGENT) {
                agentPosition = to;
            } else if (entityType == EntityType.ENEMY) {
                enemyPositions.remove(from);
                enemyPositions.add(to);
            }

            // Move the entity
            grid[from.getX()][from.getY()] = EntityType.EMPTY;
            grid[to.getX()][to.getY()] = entityType;

            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets all phone positions on the board.
     * 
     * @return list of phone positions
     */
    public List<Position> getPhonePositions() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(phonePositions);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets the current agent position.
     * 
     * @return the agent position or null if no agent
     */
    public Position getAgentPosition() {
        lock.readLock().lock();
        try {
            return agentPosition;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets all enemy positions on the board.
     * 
     * @return list of enemy positions
     */
    public List<Position> getEnemyPositions() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(enemyPositions);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets all valid adjacent positions to the given position.
     * 
     * @param position the center position
     * @return list of valid adjacent positions
     */
    public List<Position> getAdjacentPositions(Position position) {
        List<Position> adjacent = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            Position newPos = new Position(position.getX() + dir[0], position.getY() + dir[1]);
            if (isValidPosition(newPos)) {
                adjacent.add(newPos);
            }
        }

        return adjacent;
    }

    /**
     * Displays the current state of the board to the console.
     */
    public void display() {
        lock.readLock().lock();
        try {
            System.out.println("\n" + "=".repeat(SIZE * 2 + 3));
            System.out.print("  ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(j + " ");
            }
            System.out.println();
            
            for (int i = 0; i < SIZE; i++) {
                System.out.print(i + " ");
                for (int j = 0; j < SIZE; j++) {
                    System.out.print(grid[i][j].getSymbol() + " ");
                }
                System.out.println();
            }
            System.out.println("=".repeat(SIZE * 2 + 3));
        } finally {
            lock.readLock().unlock();
        }
    }
}
