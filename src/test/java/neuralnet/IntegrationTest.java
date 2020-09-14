package neuralnet;

import neuralnet.net.NeuralNetwork;
import org.junit.Test;

public class IntegrationTest {

    @Test
    public void testWithGenerations() {
        NeuralNetwork net = new NeuralNetwork(9, 10, 10, 4);        // first layer input values is taken from settings

        Generation gen;
        for (int i = 0; i < 10; i++) {
            gen = new Generation(50);
            net = gen.run(net);
        }
    }
}
