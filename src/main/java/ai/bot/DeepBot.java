package ai.bot;

import application.GamePanel;
import neuralnet.BoardAdapter;
import neuralnet.Generation;
import neuralnet.net.NeuralNetwork;
import util.Direction;
import util.Setting;

import java.util.ArrayList;
import java.util.List;

public class DeepBot extends Bot {

    List<NeuralNetwork> netList = new ArrayList<>();
    NeuralNetwork best = null;
    BoardAdapter adapter = null;
    {
        NeuralNetwork net = new NeuralNetwork(Setting.getSettings().getNetParams());  // TODO: connect
        //NeuralNetwork net = new NeuralNetwork(4, 10, 3, 7, 4);

        Generation gen;
        for (int i = 0; i < Setting.getSettings().getGenerationCount(); i++) {
            gen = new Generation(Setting.getSettings().getPopulationSize());
            net = gen.run(net);
            netList.add(net);
        }

        best = netList.get(netList.size()-1);
        adapter = new BoardAdapter(GamePanel.getBoard(), best);
    }


    @Override
    protected void run() {
        Direction d = adapter.getDirection(GamePanel.getBoard());
        running = GamePanel.move(d);
    }

    @Override
    protected List<Direction> getPath() {
        return null;
    }
}
