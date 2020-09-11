package neuralnet;

import ai.PathGenerator;
import game.Board;
import util.Direction;

import java.util.Collections;
import java.util.List;

import static util.Setting.BOARD_HEIGHT;
import static util.Setting.BOARD_WIDTH;

public class BoardAdapter {

    NeuralNetwork net;
    Board board = new Board();

    public BoardAdapter(NeuralNetwork net) {
        this.net = net;
    }

    public boolean moveSnake() {
        return process();
    }

    private boolean process() {
        List<int[]> snake = board.getSnake();
        int[] snakeHead = snake.get(0);
        int[] goodie = board.getGoodie();

        int distWallLeft = snakeHead[0] > 0 ? 1 : -1;
        int distWallRight = Math.abs(BOARD_WIDTH - snakeHead[0]) > 0 ? 1 : -1;
        int distWallUp = snakeHead[1] > 0 ? 1 : -1;
        int distWallDown = Math.abs(BOARD_HEIGHT - snakeHead[1]) > 0 ? 1 : -1;

        int left = (PathGenerator.exists(snake, Direction.getNextCoord(snakeHead, Direction.LEFT))) ? -1 : 1;
        int right = (PathGenerator.exists(snake, Direction.getNextCoord(snakeHead, Direction.RIGHT))) ? -1 : 1;
        int up = (PathGenerator.exists(snake, Direction.getNextCoord(snakeHead, Direction.UP))) ? -1 : 1;
        int down = (PathGenerator.exists(snake, Direction.getNextCoord(snakeHead, Direction.DOWN))) ? -1 : 1;

        int distToGoodie = Math.abs(snakeHead[0] - goodie[0]) + Math.abs(snakeHead[1] - goodie[1]);
        //double[] inputNodes = {distWallLeft, distWallRight, distWallUp, distWallDown, left, right, up, down, distToGoodie};
        double[] inputNodes = {distWallLeft, distWallRight, distWallUp, distWallDown};
        List<Double> out = net.predict(inputNodes);
        //System.out.println("NETWORK RESULT IS: " + out);
        int maxIndex = out.indexOf(Collections.max(out));

        boolean result;
        if (maxIndex == 0) {
            result = moveSnake(Direction.LEFT);
        } else if (maxIndex == 1) {
            result = moveSnake(Direction.RIGHT);
        } else if (maxIndex == 2) {
            result = moveSnake(Direction.UP);
        } else {
            result = moveSnake(Direction.DOWN);
        }

        return result;      // true = still alive
    }

    public int getFitness() {
        return board.getFitness();
    }

    private boolean moveSnake(Direction dir) {
        return board.moveSnake(dir);
    }

    public NeuralNetwork getNet() {
        return net;
    }

}
