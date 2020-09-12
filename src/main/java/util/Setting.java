package util;

import ai.bot.Bot;
import ai.bot.DeepBot;

import java.util.Random;
import java.util.function.Supplier;

public class Setting {

    private Setting() {}

    public static final ColorScheme COLOR_SCHEME = ColorScheme.CLASSIC;
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 15;
    public static final int SPEED = 200;
    public static final int BOT_SPEED = 20;

    public static final boolean HASBOT = false;
    public static final Random RANDOM = new Random();

    public static Supplier<Bot> BOT = DeepBot::new;
}
