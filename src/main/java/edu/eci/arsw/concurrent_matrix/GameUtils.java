package edu.eci.arsw.concurrent_matrix;

import java.util.*;

/**
 * Utility class containing game logic, including A* pathfinding algorithm.
 */
public class GameUtils {

    /**
     * Finds the next move towards the closest goal using A* pathfinding algorithm.
     * 
     * @param board the game board
     * @param start the starting position
     * @param goals list of goal positions
     * @return the next position to move to, or null if no path exists
     */
    public static Position findNextMoveTowards(Board board, Position start, List<Position> goals) {
        if (goals.isEmpty()) {
            return null;
        }

        Position closestGoal = findClosestGoal(start, goals);
        return findNextMoveAStar(board, start, closestGoal);
    }

    /**
     * Finds the closest goal from the given start position.
     * 
     * @param start the starting position
     * @param goals list of goal positions
     * @return the closest goal position
     */
    private static Position findClosestGoal(Position start, List<Position> goals) {
        Position closest = goals.get(0);
        int minDistance = start.distanceTo(closest);

        for (Position goal : goals) {
            int distance = start.distanceTo(goal);
            if (distance < minDistance) {
                minDistance = distance;
                closest = goal;
            }
        }

        return closest;
    }

    /**
     * Implements A* pathfinding algorithm to find the next move.
     * 
     * @param board the game board
     * @param start the starting position
     * @param goal the goal position
     * @return the next position to move to, or null if no path exists
     */
    private static Position findNextMoveAStar(Board board, Position start, Position goal) {
        if (start.equals(goal)) {
            return null;
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getFScore));
        Set<Position> closedSet = new HashSet<>();
        Map<Position, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, 0, start.distanceTo(goal), null);
        openSet.add(startNode);
        allNodes.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            
            if (current.position.equals(goal)) {
                // Reconstruct path and return first move
                return reconstructFirstMove(current, start);
            }

            closedSet.add(current.position);

            for (Position neighbor : board.getAdjacentPositions(current.position)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                EntityType neighborEntity = board.getEntity(neighbor);
                if (neighborEntity == EntityType.OBSTACLE || 
                    (neighborEntity != EntityType.EMPTY && neighborEntity != EntityType.PHONE && neighborEntity != EntityType.AGENT)) {
                    continue;
                }

                int tentativeGScore = current.gScore + 1;
                Node neighborNode = allNodes.get(neighbor);

                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, tentativeGScore, 
                                          neighbor.distanceTo(goal), current);
                    allNodes.put(neighbor, neighborNode);
                    openSet.add(neighborNode);
                } else if (tentativeGScore < neighborNode.gScore) {
                    openSet.remove(neighborNode);
                    neighborNode.gScore = tentativeGScore;
                    neighborNode.fScore = tentativeGScore + neighbor.distanceTo(goal);
                    neighborNode.parent = current;
                    openSet.add(neighborNode);
                }
            }
        }

        return null; // No path found
    }

    /**
     * Reconstructs the path and returns the first move from start.
     * 
     * @param goalNode the goal node reached by A*
     * @param start the starting position
     * @return the first move position
     */
    private static Position reconstructFirstMove(Node goalNode, Position start) {
        Node current = goalNode;
        while (current.parent != null && !current.parent.position.equals(start)) {
            current = current.parent;
        }
        return current.position;
    }

    /**
     * Checks if there's any path from start to any of the goals.
     * 
     * @param board the game board
     * @param start the starting position
     * @param goals list of goal positions
     * @return true if at least one path exists
     */
    public static boolean hasPathToAnyGoal(Board board, Position start, List<Position> goals) {
        for (Position goal : goals) {
            if (findNextMoveAStar(board, start, goal) != null || start.equals(goal)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the best move for an enemy to catch the agent.
     * 
     * @param board the game board
     * @param enemyPosition the enemy's current position
     * @param agentPosition the agent's current position
     * @return the next position for the enemy to move to
     */
    public static Position findBestMoveTowardsAgent(Board board, Position enemyPosition, Position agentPosition) {
        if (agentPosition == null) {
            return null;
        }

        return findNextMoveAStar(board, enemyPosition, agentPosition);
    }

    /**
     * Inner class representing a node in the A* algorithm.
     */
    private static class Node {
        Position position;
        int gScore;
        int fScore;
        Node parent;

        Node(Position position, int gScore, int hScore, Node parent) {
            this.position = position;
            this.gScore = gScore;
            this.fScore = gScore + hScore;
            this.parent = parent;
        }

        int getFScore() {
            return fScore;
        }
    }
}
