package util;

import ai.bot.AStarBot;
import ai.bot.Bot;
import ai.bot.DeepBot;
import ai.bot.HamiltonianBot;

import java.util.function.Supplier;

public enum Mode {

    MANUAL(false, null),
    NEURAL_NETWORK(true, DeepBot::new),
    HAMILTONIAN(true, HamiltonianBot::new),
    AStar(true, AStarBot::new);

    private boolean isBot;
    private Supplier<Bot> template;

    Mode(boolean isBot, Supplier<Bot> template) {
        this.isBot = isBot;
        this.template = template;
    }

    public boolean isBot() {
        return isBot;
    }

    public Supplier<Bot> getBotTemplate() {
        return template;
    }
}
