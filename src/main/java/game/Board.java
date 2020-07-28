package game;

import util.Direction;

import java.util.List;

public class Board {

    public static final int BOARD_WIDTH = 15;
    public static final int BOARD_HEIGHT = 12;
    private Snake snake = new Snake();
    private int[] goodie = new int[] {BOARD_WIDTH-1, BOARD_HEIGHT-1};
    private int result = 0;


    public boolean moveSnake(Direction dir) {
        snake.move(dir, goodie);
        if (snake.isAlive() && !snake.isWinner()) {
            return true;
        }
        result = snake.isWinner() ? 1 : -1;
        return false;
    }

    public int getResult() {
        return result;
    }

    public boolean isGameFinished() {
        return result != 0;
    }

    public List<int[]> getSnake() {
        return snake.getBody();
    }

    public int[] getGoodie() {
        return goodie;
    }

}
