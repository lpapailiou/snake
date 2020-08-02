package ai;

import util.Direction;

import java.util.*;

public class AStarPathGenerator extends PathGenerator{

    public static List<int[]> getAStarPath(List<int[]> availableNodes, int[] start, int[] end) {
        List<int[]> path = new ArrayList<>();
        Set<Node> graph = buildGraph(availableNodes, start, end);
        Set<PathNode> way = new HashSet<>();
        Node startNode = getNode(graph, start);
        startNode.visited = true;
        PathNode currentNode = null;

        currentNode = new PathNode(startNode, null);

        way.add(currentNode);
        boolean endFound = false;
        PATHFINDER: while (!endFound) {
            Set<PathNode> nodesToCheck = new HashSet<>();
            Set<Node> neighbors = currentNode.neighbors;
            for (Node neigh : neighbors) {
                if (Arrays.equals(neigh.coordinate, end)) {
                    endFound = true;
                }
                if (!neigh.visited) {
                    PathNode newNode = new PathNode(neigh, currentNode);
                    nodesToCheck.add(newNode);
                    neigh.visited = true;
                }
            }

            if (!nodesToCheck.isEmpty()) {
                currentNode = Collections.min(nodesToCheck);
                way.add(currentNode);
            } else {
                if (currentNode.prev != null) {
                    for (Node neighbor : currentNode.prev.neighbors) {
                        if (Arrays.equals(neighbor.coordinate, currentNode.coordinate)) {
                            neighbor.baseScore += 1000;
                            currentNode.prev.neighbors.remove(neighbor);
                            break;
                        }
                    }
                    currentNode = currentNode.prev;
                    System.out.println("OOPS!");       // TODO: check why it did not work

                } else {
                    System.out.println("NOPE!");       // TODO: check why it did not work
                    return availableNodes;
                }
            }
        }


        boolean stop = false;
        while (!stop) {
            stop = currentNode.prev == null;
            path.add(0, currentNode.coordinate);
            currentNode = currentNode.prev;
        }

        return path;
    }

    public static Set<Node> buildGraph(List<int[]> availableNodes, int[] start, int[] end) {
        Set<Node> connections = new HashSet<>();
        Direction[] dirs = Direction.getDirections();
        for (int[] node: availableNodes) {
            Node n = new Node(node, Math.abs(node[0] - end[0]) + Math.abs(node[1] - end[1]));
            connections.add(n);
        }
        for (Node node: connections) {
            Set<Node> adj = new HashSet<>();
            for (Direction dir : dirs) {
                int[] coord = Direction.getNextCoord(node.coordinate, dir);
                Node neighbor = nodeExists(connections, coord);
                if (neighbor != null) {
                    adj.add(neighbor);
                }
            }
            node.neighbors = adj;
        }
        return connections;
    }

    public static Node nodeExists(Set<Node> list, int[] block) {
        for (Node node : list) {
            if (Arrays.equals(node.coordinate, block)) {
                return node;
            }
        }
        return null;
    }

    private static Node getNode(Set<Node> nodes, int[] coordinate) {
        for (Node no : nodes) {
            if (Arrays.equals(no.coordinate, coordinate)) {
                return no;
            }
        }
        return null;
    }

    static class Node {
        Set<Node> neighbors = new HashSet<>();
        int[] coordinate;
        int distance;
        int baseScore = 0;
        boolean visited = false;
        Node (int[] coord, int distance) {
            this.coordinate = coord;
            this.distance = distance;
        }
    }

    static class PathNode extends Node implements Comparable<PathNode> {
        PathNode prev;
        int score;
        int weight = 1;
        PathNode(Node node, PathNode prev) {
            super(node.coordinate, node.distance);
            this.prev = prev;
            this.neighbors = node.neighbors;
            this.visited = node.visited;
            this.baseScore = node.baseScore;
            if (prev != null) {
                weight = weight + prev.weight;
                score = baseScore + weight + prev.weight + distance;
            } else {
                visited = true;
                score = baseScore + weight + distance;
            }
        }

        void print() {
            System.out.println("path node: " + Arrays.toString(coordinate) + ", score: " + score);
        }

        @Override
        public int compareTo(PathNode o) {
            if (this.score > o.score) {
                return 1;
            } else if (this.score < o.score) {
                return -1;
            }
            return 0;
        }
    }
}
