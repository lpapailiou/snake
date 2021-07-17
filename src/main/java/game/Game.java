package game;

import main.Config;
import main.Direction;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Random;

import static ai.netadapter.InputNode.exists;

public class Game {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Snake snake = new Snake();
    private int[] goodie;
    private int result = 0;
    private int moveCounter = 0;
    private int boardWith = Config.getInstance().getBoardWidth();
    private int boardHeight = Config.getInstance().getBoardHeight();

    public Game() {
        setGoodie();
    }

    private void setGoodie() {
        int[] newGoodie = null;
        while (newGoodie == null || exists(snake.getBody(), newGoodie)) {
            newGoodie = new int[] {new Random().nextInt(boardWith), new Random().nextInt(boardHeight)};
        }
        goodie = newGoodie;
    }

    public void addListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("tick", l);
    }

    public boolean moveSnake(Direction dir) {
        return validateMove(snake.move(dir, goodie));
    }

    public boolean moveSnake(int[] coord) {
        return validateMove(snake.move(coord, goodie));
    }

    private boolean validateMove(boolean isNewGoodieRequired) {
        pcs.firePropertyChange("tick", false, true);
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
        int snakeLength = snake.getBody().size();
        int stepsPerSnake = (int) (moveCounter / snakeLength);
        int boardHalf = (boardWith + boardHeight)/2;

        if (snakeLength < boardHalf * 1.5) {
            return (long) Math.pow(snakeLength, 3.7) + moveCounter;
        }
        return (long) (Math.pow(snakeLength, 4.7) - (moveCounter/snakeLength));
/*
int snakeLength = snake.getBody().size();
            return (long) Math.pow(snakeLength, 3.7) + moveCounter;

        int winnerPoints = result * 100;
        int snakeLength = snake.getBody().size();
        return winnerPoints + snakeLength*50 + moveCounter;

        int snakeLength = snake.getBody().size();
        if (snakeLength < 10) {
            return (long) (moveCounter*moveCounter * Math.pow(2, snakeLength));
        }
        return (long) (moveCounter*moveCounter*Math.pow(2, 10) * (snakeLength-9));*/
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
