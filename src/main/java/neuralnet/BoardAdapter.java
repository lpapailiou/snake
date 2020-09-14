package neuralnet;

import ai.PathGenerator;
import game.Board;
import neuralnet.net.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardAdapter {

    NeuralNetwork net;
    Board board = new Board();

    public BoardAdapter(NeuralNetwork net) {
        this.net = net;
    }

    public BoardAdapter(Board board, NeuralNetwork net) {
        this.board = board;
        this.net = net;
    }

    public boolean moveSnake() {
        return process();
    }

    public Direction getDirection(Board board) {
        List<int[]> snake = board.getSnake();
        int[] goodie = board.getGoodie();

        double[] inputValues = new double[Setting.getSettings().getNodeSelection().size()]; //new double[ordinals.size()]; // TODO: add back in
        int arrayIndex = 0;
        for (Integer index : Setting.getSettings().getNodeSelection()) {
            inputValues[arrayIndex] = InputNode.values()[index].getInputValue(snake, goodie);
            arrayIndex++;
        }
        List<Double> out = net.predict(inputValues);
        //List<Double> out = net.learn(inputValues, null);
        //System.out.println("NETWORK RESULT IS: " + out);
        int maxIndex = out.indexOf(Collections.max(out));

        Direction result;
        if (maxIndex == 0) {
            result = Direction.LEFT;
        } else if (maxIndex == 1) {
            result = Direction.RIGHT;
        } else if (maxIndex == 2) {
            result = Direction.UP;
        } else {
            result = Direction.DOWN;
        }

        return result;      // true = still alive
    }

    private boolean process() {
        List<int[]> snake = board.getSnake();
        int[] snakeHead = snake.get(0);
        int[] goodie = board.getGoodie();

        double[] inputValues = new double[Setting.getSettings().getNodeSelection().size()];
        int arrayIndex = 0;
        for (Integer index : Setting.getSettings().getNodeSelection()) {
            inputValues[arrayIndex] = InputNode.values()[index].getInputValue(snake, goodie);
            arrayIndex++;
        }
        List<Double> out = net.predict(inputValues);
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

    public boolean moveSnake(Direction dir) {
        return board.moveSnake(dir);
    }

    public NeuralNetwork getNet() {
        return net;
    }

}
