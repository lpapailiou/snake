package util;

import java.util.Random;

public enum Direction {

    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1),
    UP_LEFT(-1, -1),
    UP_RIGHT(1, -1),
    DOWN_LEFT(-1, 1),
    DOWN_RIGHT(1, 1),
    NONE(0, 0),
    GONE(-9, 0);

    private int x;
    private int y;
    private static final Direction[] dirList = {LEFT, RIGHT, UP, DOWN};

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
        int random = Setting.getSettings().getRandom().nextInt(dirList.length);
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
