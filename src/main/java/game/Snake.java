package game;

import util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static game.Board.BOARD_HEIGHT;
import static game.Board.BOARD_WIDTH;

public class Snake {

    private List<int[]> snake = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private boolean isAlive = true;
    private boolean isWinner = false;

    public Snake() {
        snake.add(new int[] {0, 0});
    }

    public void move(Direction dir, int[] goodie) {
        int[] coord = new int[] {snake.get(0)[0]+dir.getX(), snake.get(0)[1]+dir.getY()};
        if (!isAlive) {
            return;
        } else if (!isOnBoard(coord) || isGoingBackwards(coord)) {
            isAlive = false;
            return;
        } else if (isPartOfSnake(coord)) {
            isAlive = false;
            return;
        }
        snake.add(0, coord.clone());
        if (!Arrays.equals(coord, goodie)) {
            snake.remove(snake.size() - 1);
        } else {
            if (snake.size() < BOARD_WIDTH*BOARD_HEIGHT) {
                setNewGoodie(goodie);
            } else {
                isWinner = true;
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public List<int[]> getBody() {
        return snake;
    }

    private void setNewGoodie(int[] goodie) {
        int[] newGoodie = new int[] {RANDOM.nextInt(BOARD_WIDTH), RANDOM.nextInt(BOARD_HEIGHT)};
        if (isPartOfSnake(newGoodie)) {
            setNewGoodie(goodie);
        } else {
            goodie[0] = newGoodie[0];
            goodie[1] = newGoodie[1];
        }
    }

    private boolean isOnBoard(int[] coord) {
        if (coord[0] < 0 || coord[0] >= BOARD_WIDTH) {
            return false;
        } else if (coord[1] < 0 || coord[1] >= BOARD_HEIGHT) {
            return false;
        }
        return true;
    }

    private boolean isGoingBackwards(int[] coord) {
        if (snake.size() > 1) {
            if (Arrays.equals(snake.get(1), coord)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPartOfSnake(int[] coord) {
        for (int[] block : snake) {
            if (Arrays.equals(block, coord)) {
                return true;
            }
        }
        return false;
    }

}
