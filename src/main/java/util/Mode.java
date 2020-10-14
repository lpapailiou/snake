package util;

import ai.bot.*;

import java.util.function.Supplier;

public enum Mode {

    MANUAL("classic arcade mode (manual)", null),
    NEURAL_NETWORK("neural network", DeepBot::new),
    HAMILTONIAN("hamiltonian cycle", HamiltonianBot::new),
    HAMILTONIAN_SHORTCUT("hamiltonian cycle \\w shortcut", HamiltonianShortcutBot::new),
    AStar("A* algorithm", AStarBot::new);

    private String label;
    private Supplier<Bot> template;

    Mode(String label, Supplier<Bot> template) {
        this.label = label;
        this.template = template;
    }

    public String getLabel() {
        return label;
    }

    public boolean isBot() {
        return !(template == null);
    }

    public Supplier<Bot> getBotTemplate() {
        return template;
    }
}
