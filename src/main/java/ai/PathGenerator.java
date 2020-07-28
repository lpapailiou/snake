package ai;

import util.Direction;

import java.util.*;

import static util.Setting.BOARD_HEIGHT;
import static util.Setting.BOARD_WIDTH;

public class PathGenerator {

    public static List<Direction> getPath(List<int[]> snake, int[] goodie) {
        int[] distance = new int[] {goodie[0]-snake.get(0)[0], goodie[1]-snake.get(0)[1]};
        List<Direction> path = getShortestPath(distance);
        if (!path.isEmpty() && snake.size() > 1) {
            int counter = path.size();
            while (counter > 0 && Arrays.equals(new int[]{snake.get(0)[0] + path.get(path.size()-1).getX(), snake.get(0)[1] + path.get(path.size() - 1).getY()}, snake.get(1))) {
                System.out.println("shuffle");
                counter--;
                Collections.shuffle(path);
            }
        }
        System.out.println("generated path: " + path.toString());
        return path;
    }

    public static List<Direction> getExploringPath(List<int[]> snake, int[] goodie) {
        List<Direction> path = new ArrayList<>();
        HashSet<Direction> dirsTested = new HashSet<>();
        List<int[]> fakeSnake = new ArrayList<>(snake);
        int[] current = snake.get(0);
        Direction dir = Direction.getRandomDirection();
        int[] next = new int[] {current[0]+dir.getX(), current[1]+dir.getY()};
        boolean goodieReached = false;
        while (!goodieReached) {
            if (!exists(fakeSnake, next) && isValid(next)) {
                path.add(0, dir);
                fakeSnake.add(0, next.clone());
                fakeSnake.remove(fakeSnake.size()-1);
                current = next.clone();
                goodieReached = Arrays.equals(goodie, current);
                dirsTested.clear();
            } else {
                dirsTested.add(dir);
                if (dirsTested.size() == 4) {
                    return getExploringPath(snake, goodie);
                }
            }
            if (!goodieReached) {
                dir = Direction.getRandomDirection();
                next = new int[]{current[0] + dir.getX(), current[1] + dir.getY()};

            }
        }
        return path;
    }

    public static List<Direction> getHamiltonPath(List<int[]> snake) {
        List<Direction> path = new ArrayList<>();
        HashSet<Direction> dirsTested = new HashSet<>();
        List<int[]> fakeSnake = new ArrayList<>(snake);
        int[] current = snake.get(0);
        Direction dir = Direction.getRandomDirection();
        int[] next = new int[] {current[0]+dir.getX(), current[1]+dir.getY()};
        while (fakeSnake.size() < BOARD_WIDTH*BOARD_HEIGHT) {
            if (!exists(fakeSnake, next) && isValid(next)) {
                path.add(0, dir);
                fakeSnake.add(0, next.clone());
                current = next.clone();
                dirsTested.clear();
            } else {
                dirsTested.add(dir);
                if (dirsTested.size() == 4) {
                    dirsTested.clear();
                    return getHamiltonPath(snake);
                }
            }
            dir = Direction.getRandomDirection();
            next = new int[]{current[0] + dir.getX(), current[1] + dir.getY()};
        }
        return path;
    }

    private static boolean isValid(int[] next) {
        if (next[0] < 0 || next[0] >= BOARD_WIDTH) {
            return false;
        } else if (next[1] < 0 || next[1] >= BOARD_HEIGHT) {
            return false;
        }
        return true;
    }

    private static List<Direction> getShortestPath(int[] direction) {
        List<Direction> dirs = new ArrayList<>();
        for (int i = 0; i < Math.abs(direction[0]); i++) {
            if (direction[0] < 0) {
                dirs.add(Direction.LEFT);
            } else {
                dirs.add(Direction.RIGHT);
            }
        }

        for (int i = 0; i < Math.abs(direction[1]); i++) {
            if (direction[1] < 0) {
                dirs.add(Direction.UP);
            } else {
                dirs.add(Direction.DOWN);
            }
        }
        Collections.shuffle(dirs);
        return dirs;
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
