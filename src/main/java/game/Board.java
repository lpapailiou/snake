package game;

import util.Direction;

import java.util.Arrays;
import java.util.List;

import static util.Setting.*;

public class Board {

    private Snake snake = new Snake();
    private int[] goodie;
    private int result = 0;

    public Board() {
        int[] snakePos = snake.getBody().get(0);
        goodie = getRandomGoodie();
        while (Arrays.equals(snakePos, goodie)) {
            goodie = getRandomGoodie();
        }
    }

    private int[] getRandomGoodie() {
        return new int[] {RANDOM.nextInt(BOARD_WIDTH), RANDOM.nextInt(BOARD_HEIGHT)};
    }


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
