package game;

import ai.bot.DeepBot;
import util.Direction;
import util.Setting;

import java.util.List;

import static ai.PathGenerator.exists;
import static util.Setting.*;

public class Board {

    private Snake snake = new Snake();
    private int[] goodie;
    private int result = 0;
    private int moveCounter = 0;

    public Board() {
        setGoodie();
    }

    private void setGoodie() {
        int[] newGoodie = null;
        while (newGoodie == null || exists(snake.getBody(), newGoodie)) {
            newGoodie = new int[] {Setting.getSettings().getRandom().nextInt(Setting.getSettings().getBoardWidth()), Setting.getSettings().getRandom().nextInt(Setting.getSettings().getBoardHeight())};
        }
        goodie = newGoodie;
    }

    public boolean moveSnake(Direction dir) {
        return validateMove(snake.move(dir, goodie));
    }

    public boolean moveSnake(int[] coord) {
        return validateMove(snake.move(coord, goodie));
    }

    private boolean validateMove(boolean isNewGoodieRequired) {
        if (snake.isAlive() && !snake.isWinner()) {
            moveCounter++;
            if (isNewGoodieRequired) {
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

    public long getFitness() {
        /*
        int winnerPoints = result * 100;
        int snakeLength = snake.getBody().size();
        return winnerPoints + snakeLength*50 + moveCounter;*/
        int snakeLength = snake.getBody().size();
        if (snakeLength < 10) {
            return (long) (moveCounter*moveCounter * Math.pow(2, snakeLength));
        }
        return (long) (moveCounter*moveCounter*Math.pow(2, 10) * (snakeLength-9));
    }

    public boolean isGameFinished() {
        return result != 0;
    }

    public List<int[]> getSnake() {
        return snake.getBody();
    }

    public Snake getRealSnake() {return snake; }

    public int[] getGoodie() {
        return goodie;
    }

}
