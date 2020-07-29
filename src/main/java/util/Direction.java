package util;

import java.util.Random;

import static util.Setting.RANDOM;

public enum Direction {

    NONE(0, 0),
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    GONE(-9, 0);

    private int x;
    private int y;
    private static final Direction[] dirList = {UP, DOWN, LEFT, RIGHT};

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

    public static Direction getRandomDirection() {
        int random = RANDOM.nextInt(dirList.length);
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
        return Direction.NONE;
    }

    public int[] asArray() {
        return new int[] {x, y};
    }
}
