package ai;

import util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ai.PathGenerator.intersects;
import static ai.PathGenerator.intersectsAt;
import static ai.PathUtils.*;
import static util.Setting.*;

public class HamiltonPath {

    public static List<Direction> getStartCircle() {
        List<Direction> circle = new ArrayList();
        circle.add(Direction.UP);
        circle.add(Direction.RIGHT);
        circle.add(Direction.DOWN);
        circle.add(Direction.LEFT);
        return circle;
    }

    public static List<Direction> getHamilton(int[] start) {
        List<Direction> init = getStartCircle();
        List<int[]> path = PathUtils.getPathFromDirections(start, init);
        path.add(0, start);
        int threshold = BOARD_WIDTH * BOARD_HEIGHT;
        int counter = threshold*7;
        while (path.size() < threshold) {
            counter--;
            //addSegment(path);
            stretchCircle(path);
            if (counter == 0 && path.size() < threshold) {
                return null;
            }
        }

        System.out.println(counter);

        System.out.println("path: " + path.size());
        path.remove(0);
        print(path);
        return PathUtils.getDirectionsFromPath(start, path);
    }

    private static void print(List<int[]> list) {
        for (int[] i : list) {
            System.out.print(Arrays.toString(i) + ", ");
        }
        System.out.println();
    }

    public static void stretchCircle(List<int[]> path) {
        int index = RANDOM.nextInt(path.size()-2);
        int[] coord1 = path.get(index);
        int[] coord2 = path.get(index+1);
        boolean horizontal = false;
        if (coord1[1] == coord2[1]) {
            horizontal = true;
        }
        int minmax = RANDOM.nextInt(2);
        int[] nextcoord1 = null;
        int[] nextcoord2 = null;
        if (horizontal) {
            if (minmax == 0) {
                nextcoord1 = Direction.getNextCoord(coord1, Direction.LEFT);
                if (isValid(nextcoord1) && !exists(path, nextcoord1)) {
                    nextcoord2 = Direction.getNextCoord(coord2, Direction.LEFT);
                } else {
                    nextcoord1 = Direction.getNextCoord(coord1, Direction.RIGHT);
                    if (isValid(nextcoord1) && !exists(path, nextcoord1)) {
                        nextcoord2 = Direction.getNextCoord(coord2, Direction.RIGHT);
                    } else {
                        return;
                    }
                }
            }
        } else {
            if (minmax == 0) {
                nextcoord1 = Direction.getNextCoord(coord1, Direction.UP);
                if (isValid(nextcoord1) && !exists(path, nextcoord1)) {
                    nextcoord2 = Direction.getNextCoord(coord2, Direction.UP);
                } else {
                    nextcoord1 = Direction.getNextCoord(coord1, Direction.DOWN);
                    if (isValid(nextcoord1) && !exists(path, nextcoord1)) {
                        nextcoord2 = Direction.getNextCoord(coord2, Direction.DOWN);
                    } else {
                        return;
                    }
                }
            }
        }

        if (isValid(nextcoord1) && !exists(path, nextcoord1) && isValid(nextcoord2) && !exists(path, nextcoord2)) {
            path.add(index + 1, nextcoord1);
            path.add(index + 2, nextcoord2);

            int indindex = index+1;
            boolean success = addSegment(path, indindex);
            while (success) {
                success = addSegment(path, ++indindex);
            }
        }
    }

    public static void addSegment(List<int[]> path) {

        int index = RANDOM.nextInt(path.size());
        int[] coord = path.get(index);
        List<int[]> free = getFreeNeighbour(path, coord);
        if (!free.isEmpty()) {
            int nextIndex = (index == path.size()-1) ? 0 : index+1;
            int[] othercoord = path.get(nextIndex);
            List<int[]> otherfree = getFreeNeighbour(path, othercoord);
            if (!otherfree.isEmpty()) {
                int[] first = null;
                int[] second = null;
                for (int[] i : free) {
                    second = intersectsAt(otherfree, i);
                    if (!Arrays.equals(second, new int[] {-1,-1})) {
                        first = i;
                        break;
                    }
                }
                if (first != null) {
                    path.add(index + 1, first);
                    path.add(index + 2, second);
                    int indindex = index+1;
                    boolean success = addSegment(path, indindex);
                    while (success) {
                        success = addSegment(path, ++indindex);
                    }
                }
            }
        }
    }

    public static boolean addSegment(List<int[]> path, int index1) {
        int[] coord = path.get(index1);
        List<int[]> free = getFreeNeighbour(path, coord);
        if (!free.isEmpty()) {
            int nextIndex = (index1 == path.size()-1) ? 0 : index1+1;
            int[] othercoord = path.get(nextIndex);
            List<int[]> otherfree = getFreeNeighbour(path, othercoord);
            if (!otherfree.isEmpty()) {
                int[] first = null;
                int[] second = null;
                for (int[] i : free) {
                    second = intersectsAt(otherfree, i);
                    if (!Arrays.equals(second, new int[] {-1,-1})) {
                        first = i;
                        break;
                    }
                }
                if (first != null) {
                    path.add(index1 + 1, first);
                    path.add(index1 + 2, second);
                    return true;
                }
            }
        }
        return false;
    }


    public static List<int[]> getFreeNeighbour(List<int[]> path, int[] coord) {
        List<int[]> adjacentPositions = new ArrayList<>();
        for (Direction dir : Direction.getDirections()) {
            int[] next = Direction.getNextCoord(coord, dir);
            if (isValid(next) && !exists(path, next)) {
                adjacentPositions.add(next);
            }
        }
        return adjacentPositions;
    }
}
