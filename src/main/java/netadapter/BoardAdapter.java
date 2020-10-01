package netadapter;

import application.ConfigPanel;
import game.Board;
import game.Snake;
import neuralnet.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.*;

public class BoardAdapter {

    private NeuralNetwork net;
    private Board board;
    private double[] inputValues = new double[Setting.getSettings().getNodeSelection().size()];
    private Set<Integer> nodeSelection = Setting.getSettings().getNodeSelection();

    public BoardAdapter(NeuralNetwork net) { // used by Generation for background game
        board = new Board();
        this.net = net;
    }

    public BoardAdapter(Board board, NeuralNetwork net) {   // used by bot for real game
        this.board = board;
        this.net = net;
    }

    public boolean moveSnake() {    // used by Generation
        Snake snake = board.getRealSnake();
        int[] goodie = board.getGoodie();
        Direction dir = getDirection(snake, goodie);
        boolean result = moveSnake(dir);
        return result;      // true = still alive
    }

    public Direction getDirection(Board board) {    // used by bot
        Snake snake = board.getRealSnake();
        int[] goodie = board.getGoodie();
        Direction dir = getDirection(snake, goodie);
        int ord = Arrays.asList(Direction.values()).stream().filter(d -> d == dir).findFirst().get().ordinal();
        ConfigPanel.getPanel().flashOutput(ord);
        return dir;
    }

    public Direction getDirection(Snake snake, int[] goodie) {
        int arrayIndex = 0;
        for (Integer index : nodeSelection) {
            inputValues[arrayIndex] = InputNode.values()[index].getInputValue(snake, goodie);
            arrayIndex++;
        }
        List<Double> out = net.predict(inputValues);
        //List<Double> out = net.learn(inputValues, null);
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

        return result;
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
