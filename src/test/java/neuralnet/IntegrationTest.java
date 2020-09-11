package neuralnet;

import org.junit.Test;

public class IntegrationTest {

    @Test
    public void testWithGenerations() {
        NeuralNetwork net = new NeuralNetwork(4, 10, 10, 4);

        Generation gen;
        for (int i = 0; i < 200; i++) {
            gen = new Generation(50);
            net = gen.run(net);
        }
    }
}
