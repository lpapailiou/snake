package ai.bot;

import java.util.List;

public class DeepBot extends Bot {
    @Override
    protected void run() {

    }

    @Override
    protected List<int[]> getPath() {
        return null;
    }
/*
    private NeuralNetwork currentSeedNetwork = new NeuralNetwork(Setting.getSettings().getNetParams()).setLearningRate(Setting.getSettings().getLearningRate());
    private GameDecorator adapter = new GameDecorator(GamePanel.getBoard(), currentSeedNetwork);
    private int generationCount = Setting.getSettings().getGenerationCount();
    private int populationSize = Setting.getSettings().getPopulationSize();
    private GeneticAlgorithmBatch<GameDecorator> batch = new GeneticAlgorithmBatch<>(GameDecorator.class, currentSeedNetwork, populationSize, generationCount);

    private void runGeneration() {
        currentSeedNetwork = batch.processGeneration();
        //System.out.println(currentSeedNetwork.getMutationRate());

    }


    @Override
    protected void run() {
        Direction d = adapter.getDirection(GamePanel.getBoard());
        boolean gameActive =  GamePanel.move(d);
        if (!gameActive) {
            if (currentSeedNetwork != null) {
                GamePanel.getPanel().prepareNextGeneration();
                runGeneration();
                adapter = new GameDecorator(GamePanel.getBoard(), batch.getBestNeuralNetwork());
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
    }*/
}
