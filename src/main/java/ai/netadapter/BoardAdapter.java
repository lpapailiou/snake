package ai.netadapter;

import application.NeuralNetConfigPanel;
import game.Board;
import game.Snake;
import geneticalgorithm.GeneticAlgorithmObject;
import neuralnet.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.*;

public class BoardAdapter extends GeneticAlgorithmObject<BoardAdapter> {

    private Board board;
    private double[] inputValues = new double[Setting.getSettings().getNodeSelection().size()];
    private Set<Integer> nodeSelection = Setting.getSettings().getNodeSelection();

    public BoardAdapter(NeuralNetwork net) { // used by Generation for background game
        super(net);
        board = new Board();
    }

    public BoardAdapter(Board board, NeuralNetwork net) {   // used by bot for real game
        super(net);
        this.board = board;
    }

    @Override
    public boolean executeStep() {    // used by Generation
        Snake snake = board.getRealSnake();
        int[] goodie = board.getGoodie();
        Direction dir = getDirection(snake, goodie);
        return executeStep(dir);      // true = still alive
    }

    @Override
    public boolean isImmature() {
        return getSnakeLength() < 10;
    }

    @Override
    public GeneticAlgorithmObject getGeneticAlgorithmObject(NeuralNetwork neuralNetwork) {
        return new BoardAdapter(neuralNetwork);
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
        List<Double> out = super.predict(inputValues);
        int maxIndex = out.indexOf(Collections.max(out));

        return Direction.getDirections()[maxIndex];
    }

    @Override
    public long getFitness() {
        return board.getFitness();
    }

    public int getSnakeLength() {
        return board.getSnake().size();
    }

    public boolean executeStep(Direction dir) {
        return board.moveSnake(dir);
    }

}
