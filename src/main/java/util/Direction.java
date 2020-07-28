package util;

import java.util.Random;

public enum Direction {

    NONE(0, 0),
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    GONE(-9, 0);;

    private int x;
    private int y;
    private static final Random RANDOM = new Random();

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

    public Direction getRandomDirection() {
        int random = RANDOM.nextInt(Direction.values().length);
        return Direction.values()[random];
    }
}
