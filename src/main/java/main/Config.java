package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Config {

    private static Config instance;
    private int boardWidth = 16;
    private int boardHeight = 12;
    private int initialSnakeSize = 3;

    private Theme theme = Theme.CLASSIC;
    private Mode mode = Mode.NEURAL_NETWORK;

    private int generationCount = 60;
    private int populationSize = 2000;
    private double randomizationRate = 0.8;
    private static final int NUM_INPUT_NODES = 12;
    private static final int NUM_OUTPUT_NODES = 4;
    private int[] layerConfiguration = {NUM_INPUT_NODES, 4, NUM_OUTPUT_NODES};
    private Set<Integer> inputNodeSelection = new HashSet<>();

    private Config() {
        for (int i = 0; i < layerConfiguration[0]; i++) {
            inputNodeSelection.add(i);
        }
    }

    public static synchronized Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getInitialSnakeSize() {
        return initialSnakeSize;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public int getGenerationCount() {
        return generationCount;
    }

    public void setGenerationCount(int generationCount) {
        this.generationCount = generationCount;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public double getRandomizationRate() {
        return randomizationRate;
    }

    public void setRandomizationRate(double randomizationRate) {
        this.randomizationRate = randomizationRate;
    }

    public int[] getLayerConfiguration() {
        return layerConfiguration.clone();
    }

    public List<Integer> getLayerConfigurationAsList() {
        List<Integer> layerConfigurationList = new ArrayList<>();
        for (int j : layerConfiguration) {
            layerConfigurationList.add(j);
        }
        return layerConfigurationList;
    }

    public void setLayerConfiguration(int[] layerConfiguration) {
        this.layerConfiguration = layerConfiguration;
    }

    public Set<Integer> getInputNodeSelection() {
        return new HashSet<>(inputNodeSelection);
    }

    public void addInputNode(int selectedInputNode) {
        this.inputNodeSelection.add(selectedInputNode);
    }

    public void removeInputNodeFromSelection(int selectedInputNode) {
        this.inputNodeSelection.remove(selectedInputNode);
    }

    public int getSnakeTimeout() {
        if (mode == Mode.MANUAL) {
            return Integer.MAX_VALUE;
        }
        return boardWidth * boardHeight;
    }
}
