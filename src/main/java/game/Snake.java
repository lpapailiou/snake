package game;

import main.Config;
import main.Direction;
import main.Mode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Snake {

    private List<int[]> snake = new ArrayList<>();
    private boolean isAlive = true;
    private boolean isWinner = false;
    private int moveCounter = 0;
    private int boardWidth = Config.getInstance().getBoardWidth();
    private int boardHeight = Config.getInstance().getBoardHeight();

    public Snake() {
        snake.add(new int[] {boardWidth/2, boardHeight/2});
        snake.add(Direction.getNextCoord(snake.get(0), Direction.getRandomDirection()));
    }

    public boolean move(Direction dir, int[] goodie) {
        int[] coord = new int[] {snake.get(0)[0]+dir.getX(), snake.get(0)[1]+dir.getY()};
        return move(coord, goodie);
    }

    public boolean move(int[] coord, int[] goodie) {
        moveCounter++;
        if (moveCounter > boardWidth * boardHeight) {
            if (Config.getInstance().getMode() == Mode.NEURAL_NETWORK) {
                isAlive = false;
                //System.out.println("killed snake because time limit is over");
            }
        }
        if (!isAlive) {
            return false;
        } else if (!isOnBoard(coord)) {
            //System.out.println("dies at " + Arrays.toString(coord) + " beacause fell from board");
            isAlive = false;
        } else if (isGoingBackwards(coord)) {
            //System.out.println("dies at " + Arrays.toString(coord) + " beacause is going backwards");
            isAlive = false;
        } else if (isPartOfSnake(coord)) {
            //System.out.println("dies because run into himself at " + Arrays.toString(coord));
            isAlive = false;
        }

        if (!isAlive) {
            return false;
        }

        snake.add(0, coord.clone());
        if (!Arrays.equals(coord, goodie)) {
            snake.remove(snake.size() - 1);
        } else {
            moveCounter = 0;
            if (snake.size() < boardWidth*boardHeight) {
                return true;
            } else {
                isWinner = true;
            }
        }
        return false;
    }

    public int getTimeout() { return moveCounter; }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public List<int[]> getBody() {
        return snake;
    }

    private boolean isOnBoard(int[] coord) {
        if (coord[0] < 0 || coord[0] >= boardWidth) {
            return false;
        } else return coord[1] >= 0 && coord[1] < boardHeight;
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
