package ai.bot;

import application.GamePanel;
import application.NeuralNetConfigPanel;
import ai.netadapter.BoardDecorator;
import geneticalgorithm.GeneticAlgorithmBatch;
import neuralnet.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.List;

public class DeepBot extends Bot {

    private NeuralNetwork currentSeedNetwork = new NeuralNetwork(Setting.getSettings().getLearningRate(), Setting.getSettings().getNetParams());
    private BoardDecorator adapter = new BoardDecorator(GamePanel.getBoard(), currentSeedNetwork);
    private int generationCount = Setting.getSettings().getGenerationCount();
    private int populationSize = Setting.getSettings().getPopulationSize();
    private GeneticAlgorithmBatch<BoardDecorator> batch = new GeneticAlgorithmBatch<>(BoardDecorator.class, currentSeedNetwork, populationSize, generationCount);

    private void runGeneration() {
        currentSeedNetwork = batch.processGeneration();
    }


    @Override
    protected void run() {
        Direction d = adapter.getDirection(GamePanel.getBoard());
        boolean gameActive =  GamePanel.move(d);
        if (!gameActive) {
            if (currentSeedNetwork != null) {
                GamePanel.getPanel().prepareNextGeneration();
                runGeneration();
                adapter = new BoardDecorator(GamePanel.getBoard(), batch.getBestNeuralNetwork());
                NeuralNetConfigPanel.getPanel().incGenCounter();
            } else {
                NeuralNetConfigPanel.getPanel().resetGenCounter();
                stop();
            }
        }

    }

    @Override
    protected List<int[]> getPath() {
        return null;
    }
}
