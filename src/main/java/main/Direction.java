package main;

import java.util.Random;

public enum Direction {

    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    public final int x;
    public final int y;
    private static final Direction[] dirList = {UP, RIGHT, DOWN, LEFT};

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static String[] getLabels() {
        String[] labels = new String[Direction.values().length];
        int index = 0;
        for (Direction d : Direction.values()) {
            labels[index] = d.name();
            index++;
        }
        return labels;
    }

    public static Direction getRandomDirection() {
        int random = new Random().nextInt(dirList.length);
        return dirList[random];
    }

    public static Direction[] getDirections() {
        return dirList;
    }

    public static Direction[] getAxisDirections(boolean horizontal) {
        if (horizontal) {
            return new Direction[] {Direction.LEFT, Direction.RIGHT};
        }
        return new Direction[] {Direction.UP, Direction.DOWN};
    }

    public static int[] getNextCoord(int[] start, Direction dir) {
        return new int[] {start[0]+dir.getX(), start[1]+dir.getY()};
    }

    public static Direction getDirectionFromCoordinate(int x, int y) {
        for (Direction dir : dirList) {
            if (dir.x == x && dir.y == y) {
                return dir;
            }
        }
        return null;
    }

    public int[] asArray() {
        return new int[] {x, y};
    }
}
