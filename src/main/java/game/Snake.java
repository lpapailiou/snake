package game;

import util.Direction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.Setting.*;

public class Snake {

    private List<int[]> snake = new ArrayList<>();
    private boolean isAlive = true;
    private boolean isWinner = false;

    public Snake() {
        snake.add(new int[] {BOARD_WIDTH/2, BOARD_HEIGHT/2});
    }

    public boolean move(Direction dir, int[] goodie) {
        int[] coord = new int[] {snake.get(0)[0]+dir.getX(), snake.get(0)[1]+dir.getY()};
        return move(coord, goodie);
    }

    public boolean move(int[] coord, int[] goodie) {

        if (!isAlive) {
            return false;
        } else if (!isOnBoard(coord) || isGoingBackwards(coord)) {
            if (!isOnBoard(coord)) {
                System.out.println("dies at " + Arrays.toString(coord) + " beacause fell from board");
            } else {
                System.out.println("dies at " + Arrays.toString(coord) + " beacause is going backwards");
            }
            isAlive = false;
            return false;
        } else if (isPartOfSnake(coord)) {
            System.out.println("dies because run into himself at " + Arrays.toString(coord));
            isAlive = false;
            return false;
        }
        snake.add(0, coord.clone());
        if (!Arrays.equals(coord, goodie)) {
            snake.remove(snake.size() - 1);
        } else {
            if (snake.size() < BOARD_WIDTH*BOARD_HEIGHT) {
                //setNewGoodie(goodie);
                return true;
            } else {
                isWinner = true;
            }
        }
        return false;
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
        } else return coord[1] >= 0 && coord[1] < BOARD_HEIGHT;
    }

    private boolean isGoingBackwards(int[] coord) {
        if (snake.size() > 1) {
            return Arrays.equals(snake.get(1), coord);
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
