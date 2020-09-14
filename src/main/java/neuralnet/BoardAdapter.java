package neuralnet;

import ai.PathGenerator;
import game.Board;
import neuralnet.net.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardAdapter {

    NeuralNetwork net;
    Board board = new Board();
    List<Integer> ordinals = new ArrayList<>();
    {
        ordinals.add(0);
        ordinals.add(1);
        ordinals.add(2);
        ordinals.add(3);
        ordinals.add(4);
        ordinals.add(5);
        ordinals.add(6);
        ordinals.add(7);
        ordinals.add(8);
    }

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

        double[] inputValues = new double[4]; //new double[ordinals.size()]; // TODO: add back in
        for (int i = 0; i < inputValues.length; i++) {
            inputValues[i] = InputNode.values()[ordinals.get(i)].getInputValue(snake, goodie);
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

        int distWallLeft = InputNode.values()[ordinals.get(4)].getInputValue(snake, goodie);
        int distWallRight = InputNode.values()[ordinals.get(5)].getInputValue(snake, goodie);
        int distWallUp = InputNode.values()[ordinals.get(6)].getInputValue(snake, goodie);
        int distWallDown = InputNode.values()[ordinals.get(8)].getInputValue(snake, goodie);
        System.out.println("Node 0 = " + distWallLeft + " / " + distWallRight+ " / " + distWallUp+ " / " + distWallDown);
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

    public boolean moveSnake(Direction dir) {
        return board.moveSnake(dir);
    }

    public NeuralNetwork getNet() {
        return net;
    }

}
