package ai;

import util.Direction;

import java.util.*;

public class AStarPathGenerator extends PathGenerator{

    public static List<int[]> getAStarPath(List<int[]> availableNodes, int[] start, int[] end) {
        List<int[]> path = new ArrayList<>();
        Set<Node> graph = buildGraph(availableNodes, start, end);
        Set<PathNode> way = new HashSet<>();
        Node startNode = getNode(graph, start);
        if (startNode != null) {
            PathNode startPathNode = new PathNode(startNode, null);
            way.add(startPathNode);
            PATHFINDER: while (true) {
                Set<Node> neighbors = startPathNode.neighbors;
                for (Node neigh : neighbors) {
                    PathNode newNode = new PathNode(neigh, startPathNode);
                    way.add(newNode);
                    if (Arrays.equals(neigh.coordinate, end)) {
                        break PATHFINDER;
                    }
                }
                startPathNode = Collections.min(way);
            }
        }

        //create path from min, reverse
        System.out.println(way.size());
        PathNode currNode = Collections.min(way);
        while (currNode != null) {
            path.add(0, currNode.coordinate);
            currNode = currNode.prev;
        }
        return path;
    }

    public static Set<Node> buildGraph(List<int[]> availableNodes, int[] start, int[] end) {
        Set<Node> connections = new HashSet<>();
        Direction[] dirs = Direction.getDirections();
        for (int[] node: availableNodes) {
            int distanceX = node[0] - end[0];
            int distanceY = node[1] - end[1];
            Node n = new Node(node, distanceX, distanceY);
            connections.add(n);
        }
        for (Node node: connections) {
            if (Arrays.equals(node.coordinate, start)) {
                node.weight = 0;
            }
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
        System.out.println("start: "+ Arrays.toString(coordinate));
        for (Node no : nodes) {
            System.out.println(Arrays.toString(no.coordinate));
            if (Arrays.equals(no.coordinate, coordinate)) {
                System.out.println(Arrays.toString(no.coordinate) + "FOUND");
                return no;
            }
        }
        return null;
    }

    static class Node {
        Set<Node> neighbors = new HashSet<>();
        int[] coordinate;
        int distanceX;
        int distanceY;
        int distance;
        int weight = 1;
        Node (int[] coord, int distX, int distY) {
            this.coordinate = coord;
            this.distanceX = distX;
            this.distanceY = distY;
            this.distance = Math.abs(distX) + Math.abs(distY);
        }
    }

    static class PathNode extends Node implements Comparable<PathNode> {
        PathNode prev;
        int score;
        PathNode(Node node, PathNode prev) {
            super(node.coordinate, node.distanceX, node.distanceY);
            this.prev = prev;
            if (prev != null) {
                score = weight + prev.weight + distance;
            } else {
                score = weight + distance;
            }
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
