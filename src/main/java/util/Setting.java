package util;

import ai.bot.Bot;
import ai.bot.DeepBot;
import java.util.Random;
import java.util.function.Supplier;

public class Setting {

    private static Setting instance;

    private ColorScheme colorScheme = ColorScheme.CLASSIC;
    private int boardWidth = 20;
    private int boardHeight = 15;
    private int speed = 200;
    private int botSpeed = 100;
    private int neuralBotTimeout = 500;
    private boolean hasBot = false;
    private Random random = new Random();
    private Supplier<Bot> botTemplate = DeepBot::new;

    private Setting() {}

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

    public boolean hasBot() { return hasBot; }

    public Random getRandom() { return random; }

    public Bot getBot() { return botTemplate.get(); }

}
