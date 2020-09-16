package util;

import ai.bot.Bot;
import ai.bot.DeepBot;

import java.util.*;
import java.util.function.Supplier;

public class Setting {

    private static Setting instance;

    private ColorScheme colorScheme = ColorScheme.CLASSIC;
    private int boardWidth = 20;
    private int boardHeight = 15;
    private int speed = 200;
    private int botSpeed = 100;
    private int neuralBotTimeout = 100;
    private Random random = new Random();
    private Supplier<Bot> botTemplate = DeepBot::new;
    private int generationCount = 10;
    private int populationSize = 300;
    private double learningRate = 0.1;
    private int[] netParams = {10, 10, 8, 7, 4};
    private Set<Integer> nodeSelection = new HashSet<>();

    private Setting() {
        for (int i = 0; i < netParams[0]; i++) {
            nodeSelection.add(i);
        }
    }

    public static Setting getSettings() {
        if (instance == null) {
            instance = new Setting();
        }
        return instance;
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(ColorScheme scheme) {
        this.colorScheme = scheme;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int width) {
        this.boardWidth = width;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int height) {
        this.boardHeight = height;
    }

    public int getSpeed() {
        return speed;
    }

    public int getBotSpeed() {
        return botSpeed;
    }

    public int getNeuralBotTimeout() { return neuralBotTimeout; }

    public boolean hasBot() { return !(botTemplate == null); }

    public Random getRandom() { return random; }

    public Bot getBot() {
        if (botTemplate != null) {
            return botTemplate.get();
        }
        return null;
    }

    public void setBot(Supplier<Bot> bot) { botTemplate = bot; }

    public int getGenerationCount() {
        return generationCount;
    }

    public void setGenerationCount(int count) { generationCount = count; }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int count) { populationSize = count; }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double rate) { learningRate = rate; }

    public int[] getNetParams() { return netParams; }   // TODO: make list? proper encapsulation?

    public List<Integer> getNetParamsAsList() {
        List<Integer> list = new ArrayList<>();
        for (int param : netParams) {
            list.add(param);
        }
        return list;
    }

    public void setNetParams(int[] params) { this.netParams = params; }

    public Set<Integer> getNodeSelection() { return new HashSet<>(nodeSelection); }

    public void addNodeSelectionNode(int selection) { this.nodeSelection.add(selection); }

    public void removeNodeSelectionNode(int selection) { this.nodeSelection.remove(new Integer(selection)); }

}
