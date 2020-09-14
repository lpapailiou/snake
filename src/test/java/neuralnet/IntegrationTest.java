package neuralnet;

import neuralnet.net.NeuralNetwork;
import org.junit.Test;

import java.util.List;

public class IntegrationTest {

    @Test
    public void testWithGenerations() {
        NeuralNetwork net = new NeuralNetwork(9, 10, 10, 4);        // first layer input values is taken from settings

        Generation gen;
        for (int i = 0; i < 10; i++) {
            gen = new Generation(50);
            net = gen.run(net);
        }

        double[] testSet = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 15};
        List<Double> result = net.predict(testSet);

        //System.out.println("result: " + result);
        System.out.println("left:   " + result.get(0));
        System.out.println("right:  " + result.get(1));
        System.out.println("up:     " + result.get(2));
        System.out.println("down:   " + result.get(3));
    }
}
