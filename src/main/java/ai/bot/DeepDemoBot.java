package ai.bot;

import ai.netadapter.BoardDecorator;
import ai.netadapter.Serializer;
import application.GamePanel;
import application.NeuralNetConfigPanel;
import neuralnet.NeuralNetwork;
import util.Direction;

import java.util.List;

public class DeepDemoBot extends Bot {

    private NeuralNetwork best = Serializer.load();

    private BoardDecorator adapter = new BoardDecorator(GamePanel.getBoard(), best);

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
    }
}
