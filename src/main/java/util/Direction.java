package util;

import java.util.Random;

import static util.Setting.RANDOM;

public enum Direction {

    NONE(0, 0),
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    GONE(-9, 0);;

    private int x;
    private int y;

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
        Direction[] dirList = {UP, DOWN, LEFT, RIGHT};
        int random = RANDOM.nextInt(dirList.length);
        return dirList[random];
    }
}
