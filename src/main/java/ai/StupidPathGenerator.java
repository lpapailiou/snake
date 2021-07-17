package ai;

import main.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StupidPathGenerator extends PathGenerator {
/*
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

    public static List<Direction> getShortestSafePath(List<int[]> snake, int[] target) {
        snake = new ArrayList<>(snake);
        List<Direction> path = getShortestPath(snake.get(0), target);
        int index = getNextInvalidIndex(snake, path);
        if (index != -1) {
            List<Direction> safePath = new ArrayList<>();
            for (int i = path.size()-1; i >= index; i--) {
                safePath.add(0, path.get(i));
            }
            System.out.println(safePath);
            return safePath;
        }
        System.out.println(path);
        return path;
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


 */
}
