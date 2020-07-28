package game;

import util.Direction;

import java.util.Arrays;
import java.util.List;

import static ai.PathGenerator.exists;
import static util.Setting.*;

public class Board {

    private Snake snake = new Snake();
    private int[] goodie;
    private int result = 0;

    public Board() {
        setGoodie();
    }

    private void setGoodie() {
        int[] newGoodie = null;
        while (newGoodie == null || exists(snake.getBody(), newGoodie)) {
            newGoodie = new int[] {RANDOM.nextInt(BOARD_WIDTH), RANDOM.nextInt(BOARD_HEIGHT)};
        }
        goodie = newGoodie;
    }




    public boolean moveSnake(Direction dir) {
        boolean newGoodieRequired = false;
        newGoodieRequired = snake.move(dir, goodie);
        if (snake.isAlive() && !snake.isWinner()) {
            if (newGoodieRequired) {
                setGoodie();
            }
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
