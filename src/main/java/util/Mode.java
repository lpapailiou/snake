package util;

import ai.bot.*;

import java.util.function.Supplier;

public enum Mode {

    MANUAL(false, null),
    NEURAL_NETWORK(true, DeepBot::new),
    HAMILTONIAN(true, HamiltonianBot::new),
    HAMILTONIAN_SHORTCUT(true, HamiltonianShortcutBot::new),
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
