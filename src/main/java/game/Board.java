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
    private int moveCounter;
    private int result = 0;

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
        if (moveCounter > Setting.getSettings().getNeuralBotTimeout()) {
            if (Setting.getSettings().getBot() instanceof DeepBot) {
                snake.kill();
                System.out.println("killed snake because time limit is over");
            }
        }
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

    public int getFitness() {
        int winnerPoints = result * 100;
        int snakeLength = snake.getBody().size();
        int movePoints = moveCounter;
        //return winnerPoints + snakeLength - movePoints;
        return winnerPoints + snakeLength + moveCounter;
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
