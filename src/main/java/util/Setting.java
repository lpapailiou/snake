package util;

import java.util.Random;

public class Setting {

    private Setting() {}

    public static final ColorScheme COLOR_SCHEME = ColorScheme.CLASSIC;
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 15;
    public static final int SPEED = 200;
    public static final int BOT_SPEED = 20;

    public static final boolean HASBOT = true;
    public static final Random RANDOM = new Random();
}
