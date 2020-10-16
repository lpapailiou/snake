package ai.bot;

import ai.netadapter.BoardAdapter;
import ai.netadapter.Generation;
import ai.netadapter.Serializer;
import application.GamePanel;
import application.NeuralNetConfigPanel;
import neuralnet.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.List;

public class DeepDemoBot extends Bot {

    private NeuralNetwork best = Serializer.load();

    private BoardAdapter adapter = new BoardAdapter(GamePanel.getBoard(), best);

    private NeuralNetwork getBest() {
        Generation gen = new Generation(1);
        return gen.run(best);
    }


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
