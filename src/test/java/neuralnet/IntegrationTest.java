package neuralnet;

import ai.netadapter.Generation;
import org.junit.Test;
import util.Setting;

import java.util.List;

public class IntegrationTest {

    @Test
    public void testWithGenerations() {
        NeuralNetwork net = new NeuralNetwork(12, 12,12, 4);        // first layer input values is taken from settings
        Setting.getSettings().setBoardWidth(16);
        Setting.getSettings().setBoardHeight(16);
        Setting.getSettings().setLearningRate(0.8);
        Generation gen;
        for (int i = 0; i < 1000; i++) {
            System.out.print("generation: " + i + ": \t");
            gen = new Generation(2000);
            net = gen.run(net);
        }

        /*

        double[] testSet = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 15};
        List<Double> result = net.predict(testSet);

        //System.out.println("result: " + result);
        System.out.println("left:   " + result.get(0));
        System.out.println("right:  " + result.get(1));
        System.out.println("up:     " + result.get(2));
        System.out.println("down:   " + result.get(3));*/
    }
}
