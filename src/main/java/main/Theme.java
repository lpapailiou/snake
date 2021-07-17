package main;

import javafx.scene.paint.Color;
import nn.ui.color.NNGraphColor;

import static javafx.scene.paint.Color.*;

public enum Theme {

    CLASSIC(LIME, LIME, RED, RED, BLACK, true, "themeClassic.css",
            new NNGraphColor(TRANSPARENT, LIMEGREEN, LIMEGREEN.darker(), RED, BLACK, LIMEGREEN, LIMEGREEN, LIMEGREEN.darker(), LIMEGREEN.darker())),
    SANDY(web("#93826c"), web("#93826c"), web("#85a39f"), web("#85a39f"),
            web("#E7D1B5"), true, "themeSandy.css",
            new NNGraphColor(web("#E7D1B5"), web("#93826c"), web("#93826c").darker(), web("#85a39f"), web("#E7D1B5"), web("#93826c"), web("#93826c"), web("#93826c").darker(), web("#93826c").darker())),
    SUNSET(web("#ff7582"), web("#ff7582"), web("#c56d86"), MOCCASIN,
            web("#2a3950"), true, "themeSunset.css",
            new NNGraphColor(web("#2a3950"), web("#ff7582"), web("#ff7582").darker(), MOCCASIN, web("#2a3950"), web("#ff7582"), web("#ff7582"), web("#ff7582").darker(), web("#ff7582").darker())),
    CANDY(web("#ffa7a7"), web("#ffa7a7"), web("#ffcbcb"), MOCCASIN,
            web("#c9fdff"), true, "themeCandy.css",
            new NNGraphColor(web("#c9fdff"), web("#ffa7a7"), web("#ffa7a7").darker(), MOCCASIN, web("#c9fdff"), web("#ffa7a7"), web("#ffa7a7"), web("#ffa7a7").darker(), web("#ffa7a7").darker())),
    MINIMAL(BLACK, DARKGRAY, MEDIUMVIOLETRED, MEDIUMVIOLETRED, WHITE,
            false, "themeMinimal.css",
            new NNGraphColor(BLACK, BLACK, BLACK.darker(), MEDIUMVIOLETRED, WHITE, BLACK, BLACK, BLACK.darker(), BLACK.darker())),
    YB(web("#F9CC11"), web("#F9CC11"), web("#F9CC11"), web("#ffffff"),
            BLACK, true, "themeYoungBoys.css",
            new NNGraphColor(BLACK, web("#F9CC11"), web("#F9CC11").darker(), web("#ffffff"), web("#ffffff"), web("#F9CC11"), web("#F9CC11"), web("#F9CC11").darker(), web("#F9CC11").darker()));

    private Color snakeBodyColor;
    private Color frameActiveColor;
    private Color frameInactiveColor;
    private Color foodColor;
    private Color backgroundColor;
    private boolean darkTheme;
    private String css;
    private NNGraphColor nnColorPalette;

    Theme(Color snakeBodyColor, Color frameActiveColor, Color frameInactiveColor, Color foodColor,
          Color backgroundColor, boolean darkTheme, String css, NNGraphColor nnColorPalette) {
        this.snakeBodyColor = snakeBodyColor;
        this.frameActiveColor = frameActiveColor;
        this.frameInactiveColor = frameInactiveColor;
        this.foodColor = foodColor;
        this.backgroundColor = backgroundColor;
        this.darkTheme = darkTheme;
        this.css = css;
        this.nnColorPalette = nnColorPalette;
    }

    public Color getSnakeBodyColor() {
        return snakeBodyColor;
    }

    public Color getFrameActiveColor() {
        return frameActiveColor;
    }

    public Color getFrameInactiveColor() {
        return frameInactiveColor;
    }

    public Color getFoodColor() {
        return foodColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public String getCss() {
        return css;
    }

    public NNGraphColor getNnColorPalette() {
        return nnColorPalette;
    }

}
