package ai;



public class AStarPathGenerator extends PathGenerator{
/*
    public static List<int[]> getAStarPath(List<int[]> availableNodes, int[] start, int[] end, boolean minimize) {
        List<int[]> path = new ArrayList<>();
        Set<Node> graph = buildGraph(availableNodes, start, end);
        Node startNode = getNode(graph, start);

        PathNode currentNode = new PathNode(startNode, null);
        Set<PathNode> expandedNodes = new HashSet<>();
        expandedNodes.add(currentNode);
        Set<PathNode> nodesToCheck = new HashSet<>(expandedNodes);
        boolean endFound = false;
        PATHFINDER: while (!endFound) {

            for (Node neigh : currentNode.neighbors) {
                if (Arrays.equals(neigh.coordinate, end)) {
                    PathNode newNode = new PathNode(neigh, currentNode);
                    currentNode = newNode;
                    endFound = true;
                    break;
                }
                if (!pathNodeExists(expandedNodes, neigh.coordinate)) {
                    PathNode newNode = new PathNode(neigh, currentNode);
                    nodesToCheck.add(newNode);
                }
            }

            if (!nodesToCheck.isEmpty()) {
                if (!endFound) {
                    expandedNodes.add(currentNode);
                    nodesToCheck.remove(currentNode);
                    if (minimize) {
                        currentNode = Collections.min(nodesToCheck);
                    } else {
                        currentNode = Collections.max(nodesToCheck);
                    }
                }
            } else {
                throw new RuntimeException("path not found!");
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

    public static boolean pathNodeExists(Set<PathNode> list, int[] block) {
        for (PathNode node : list) {
            if (Arrays.equals(node.coordinate, block)) {
                return true;
            }
        }
        return false;
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
            if (prev != null) {
                weight = weight + prev.weight;
            }
            score = weight + distance;
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

 */
}
