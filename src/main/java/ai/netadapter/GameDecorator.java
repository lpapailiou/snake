package ai.netadapter;

import game.Game;
import game.Snake;
import main.Config;
import main.Direction;
import nn.genetic.GeneticAlgorithmObject;
import nn.neuralnet.NeuralNetwork;
import java.util.*;

public class GameDecorator extends GeneticAlgorithmObject {

    private Game game;
    private boolean isActive = true;
    private double[] inputValues = new double[Config.getInstance().getInputNodeSelection().size()];
    private Set<Integer> nodeSelection = Config.getInstance().getInputNodeSelection();
    private int boardWith = Config.getInstance().getBoardWidth();
    private int boardHeight = Config.getInstance().getBoardHeight();

    public GameDecorator(NeuralNetwork net) { // used by Generation for background game
        super(net);
        game = new Game();
    }

    public GameDecorator(Game game, NeuralNetwork net) {   // used by bot for real game
        super(net);
        this.game = game;
    }

    @Override
    public boolean perform() {    // used by Generation
        Snake snake = game.getRealSnake();
        int[] goodie = game.getGoodie();
        Direction dir = getDirection(snake, goodie);
        isActive = perform(dir);      // true = still alive
        return isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isImmature() {
        return getSnakeLength() < 10;
    }

    public Direction getDirection(Game game) {    // used by bot
        Snake snake = game.getRealSnake();
        int[] goodie = game.getGoodie();
        Direction dir = getDirection(snake, goodie);
        int ord = Arrays.asList(Direction.values()).stream().filter(d -> d == dir).findFirst().get().ordinal();
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

    public Game getGame() {
        return game;
    }

    @Override
    public String getLogMessage() {
        return "snake length: " + game.getSnake().size();
    }

    @Override
    public long getFitness() {
        return game.getFitness();
    }

    @Override
    public boolean hasReachedGoal() {
        return game.getSnake().size() == boardWith * boardHeight;
    }

    public int getSnakeLength() {
        return game.getSnake().size();
    }

    public boolean perform(Direction dir) {
        return game.moveSnake(dir);
    }

}
