package netadapter;

import application.ConfigPanel;
import application.NeuralNetConfigPanel;
import game.Board;
import game.Snake;
import neuralnet.NeuralNetwork;
import org.jetbrains.annotations.NotNull;
import util.Direction;
import util.Setting;

import java.util.*;

public class BoardAdapter implements Comparable<BoardAdapter>{

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
        NeuralNetConfigPanel.getPanel().flashOutput(ord);
        return dir;
    }

    public Direction getDirection(Snake snake, int[] goodie) {
        int arrayIndex = 0;
        for (Integer index : nodeSelection) {
            inputValues[arrayIndex] = InputNode.values()[index].getInputValue(snake, goodie);
            arrayIndex++;
        }
        List<Double> out = net.predict(inputValues);
        int maxIndex = out.indexOf(Collections.max(out));

        return Direction.getDirections()[maxIndex];
    }

    public long getFitness() {
        return board.getFitness();
    }

    public int getSnakeLength() {
        return board.getSnake().size();
    }

    public boolean moveSnake(Direction dir) {
        return board.moveSnake(dir);
    }

    public NeuralNetwork getNet() {
        return net;
    }

    @Override
    public int compareTo(@NotNull BoardAdapter o) {
        long thisf = this.getFitness();
        long of = o.getFitness();
        if (thisf > of) {
            return 1;
        } else if (thisf < of) {
            return -1;
        }
        return 0;
    }
}
