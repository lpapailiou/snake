package ai.bot;

import application.ConfigPanel;
import application.GamePanel;
import netadapter.BoardAdapter;
import netadapter.Generation;
import neuralnet.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.List;

public class DeepBot extends Bot {

    private NeuralNetwork best = new NeuralNetwork(Setting.getSettings().getLearningRate(), Setting.getSettings().getNetParams());
    private BoardAdapter adapter = new BoardAdapter(GamePanel.getBoard(), best);
    private int generationCount = Setting.getSettings().getGenerationCount();

    private NeuralNetwork getBest() {
        Generation gen = new Generation(Setting.getSettings().getPopulationSize());
        return gen.run(best);
    }


    @Override
    protected void run() {
        Direction d = adapter.getDirection(GamePanel.getBoard());
        boolean gameActive =  GamePanel.move(d);
        if (!gameActive) {
            generationCount--;
            if (generationCount != 0) {
                GamePanel.getPanel().prepareNextGeneration();
                best = getBest();
                adapter = new BoardAdapter(GamePanel.getBoard(), best);
                ConfigPanel.getPanel().incGenCounter();
            } else {
                ConfigPanel.getPanel().resetGenCounter();
                stop();
            }
        }

    }

    @Override
    protected List<Direction> getPath() {
        return null;
    }
}
