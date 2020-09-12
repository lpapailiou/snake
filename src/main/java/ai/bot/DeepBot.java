package ai.bot;

import application.GamePanel;
import neuralnet.BoardAdapter;
import neuralnet.Generation;
import neuralnet.NeuralNetwork;
import util.Direction;

import java.util.ArrayList;
import java.util.List;

public class DeepBot extends Bot {

    List<NeuralNetwork> netList = new ArrayList<>();
    NeuralNetwork best = null;
    BoardAdapter adapter = null;
    {
        NeuralNetwork net = new NeuralNetwork(4, 10, 10, 4);

        Generation gen;
        for (int i = 0; i < 50; i++) {
            gen = new Generation(50);
            net = gen.run(net);
            netList.add(net);
        }

        best = netList.get(netList.size()-1);
        adapter = new BoardAdapter(GamePanel.getPanel().getBoard(), best);
    }


    @Override
    protected void run() {

        while (running) {
            System.out.println("run");
            Direction d = adapter.getDirection();
            running = GamePanel.move(d);
        }
        System.out.println("stop");
    }

    @Override
    protected List<Direction> getPath() {
        return null;
    }
}
