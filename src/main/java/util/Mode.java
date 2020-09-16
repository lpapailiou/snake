package util;

import ai.bot.*;

import java.util.function.Supplier;

public enum Mode {

    MANUAL(null),
    NEURAL_NETWORK(DeepBot::new),
    HAMILTONIAN(HamiltonianBot::new),
    HAMILTONIAN_SHORTCUT(HamiltonianShortcutBot::new),
    AStar(AStarBot::new);

    private Supplier<Bot> template;

    Mode(Supplier<Bot> template) {
        this.template = template;
    }

    public boolean isBot() {
        return !(template == null);
    }

    public Supplier<Bot> getBotTemplate() {
        return template;
    }
}
