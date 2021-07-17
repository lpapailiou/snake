package ai.bot;

import java.util.List;

public class DeepDemoBot extends Bot {
    @Override
    protected void run() {

    }

    @Override
    protected List<int[]> getPath() {
        return null;
    }
/*
    private NeuralNetwork best = Serializer.load();

    private GameDecorator adapter = new GameDecorator(GamePanel.getBoard(), best);

    @Override
    protected void run() {
        Direction d = adapter.getDirection(GamePanel.getBoard());
        boolean gameActive =  GamePanel.move(d);
        if (!gameActive) {
            NeuralNetConfigPanel.getPanel().resetGenCounter();
            stop();
        }

    }

    @Override
    protected List<int[]> getPath() {
        return null;
    }*/
}
