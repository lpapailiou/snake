package ai;

import util.Direction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static util.Setting.BOARD_HEIGHT;
import static util.Setting.BOARD_WIDTH;

public class PathUtils {

    public static List<Direction> getDirectionsFromPath(int[] start, List<int[]> path) {
        List<Direction> newPath = new ArrayList<>();
        int[] current = start;
        for (int[] coord : path) {
            newPath.add(Direction.getDirectionFromCoordinate(coord[0]-current[0], coord[1]-current[1]));
            current = coord;
        }
        Collections.reverse(newPath);
        return newPath;
    }

    public static List<int[]> getPathFromDirections(int[] start, List<Direction> path) {
        List<int[]> newPath = new ArrayList<>();
        Collections.reverse(path);
        int[] current = start;
        for (Direction dir : path) {
            current = new int[] {current[0]+dir.getX(), current[1]+dir.getY()};
            newPath.add(current);
        }
        return newPath;
    }

    public static List<Direction> getShortestSafePath(List<int[]> snake, int[] target) {
        snake = new ArrayList<>(snake);
        List<Direction> path = getShortestPath(snake.get(0), target);
        int index = getNextInvalidIndex(snake, path);
        if (index != -1) {
            List<Direction> safePath = new ArrayList<>();
            for (int i = path.size()-1; i >= index; i--) {
                safePath.add(0, path.get(i));
            }
            Collections.reverse(safePath);
            System.out.println(safePath);
            Collections.reverse(safePath);
            return safePath;
        }
        Collections.reverse(path);
        System.out.println(path);
        Collections.reverse(path);
        return path;
    }

    public static int getNextInvalidIndex(List<int[]> snake, List<Direction> path) {
        snake = new ArrayList<>(snake);
        for (int i = path.size()-1; i >= 0; i--) {
            int[] next = Direction.getNextCoord(snake.get(0), path.get(i));
            if (!isValid(next) || exists(snake, next)) {
                snake.add(0, next);
            } else {
                return i;
            }
        }
        return -1;
    }



    public static List<Direction> getShortestPath(int[] start, int[] end) {
        int[] distance = new int[]{end[0]-start[0], end[1]-start[1]};
        List<Direction> dirs = new ArrayList<>();
        for (int i = 0; i < Math.abs(distance[0]); i++) {
            if (distance[0] < 0) {
                dirs.add(Direction.LEFT);
            } else {
                dirs.add(Direction.RIGHT);
            }
        }

        for (int i = 0; i < Math.abs(distance[1]); i++) {
            if (distance[1] < 0) {
                dirs.add(Direction.UP);
            } else {
                dirs.add(Direction.DOWN);
            }
        }
        Collections.shuffle(dirs);
        return dirs;
    }

    public static boolean isValid(int[] next) {
        if (next[0] < 0 || next[0] >= BOARD_WIDTH) {
            return false;
        } else return next[1] >= 0 && next[1] < BOARD_HEIGHT;
    }

    public static boolean exists(List<int[]> list, int[] block) {
        for (int i = 0; i < list.size(); i++) {
            if (Arrays.equals(list.get(i), block)) {
                return true;
            }
        }
        return false;
    }
}
