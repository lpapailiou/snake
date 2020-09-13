package util;

import javafx.scene.paint.Color;

public enum ColorScheme {

    CLASSIC(Color.BLACK, Color.LIME, Color.RED, Color.LIME, Color.LIME, Color.RED),
    MODERN(Color.WHITE, Color.DARKGRAY, Color.MEDIUMVIOLETRED, Color.DARKGRAY, Color.BLACK, Color.MEDIUMVIOLETRED),
    SANDY(Color.BURLYWOOD, Color.SIENNA, Color.RED, Color.SIENNA, Color.SIENNA, Color.RED);

    private Color background;
    private Color backgroundFrame;
    private Color backgroundFrameEnd;
    private Color text;
    private Color snake;
    private Color goodie;

    ColorScheme(Color background, Color backgroundFrame, Color backgroundFrameEnd, Color text, Color snake, Color goodie) {
        this.background = background;
        this.backgroundFrame = backgroundFrame;
        this.backgroundFrameEnd = backgroundFrameEnd;
        this.text = text;
        this.snake = snake;
        this.goodie = goodie;
    }

    public Color getBackground() {
        return background;
    }

    public Color getBackgroundFrame() {
        return backgroundFrame;
    }

    public Color getBackgroundFrameEnd() {
        return backgroundFrameEnd;
    }

    public Color getText() {
        return text;
    }

    public Color getSnake() {
        return snake;
    }

    public Color getGoodie() {
        return goodie;
    }
}
