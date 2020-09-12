package neuralnet.net;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    private List<Layer> layers = new ArrayList<>();
    private double learningRate = 0.1;

    /**
     * parameter list for matrix sizes.
     * first parameter = input nodes count
     * last parameter = output nodes count
     * parameters between = size of hidden layer
     * @param layerParams
     */
    public NeuralNetwork(int... layerParams) {
        if (layerParams.length < 2) {
            throw new IllegalArgumentException("enter at least two parameters to create neural network!");
        }
        for (int i = 1; i < layerParams.length; i++) {
            layers.add(new Layer(layerParams[i], layerParams[i-1]));
        }
    }

    private NeuralNetwork(List<Layer> layers) {
        this.layers = new ArrayList<>(layers);
    }

    // forward pass
    public List<Double> predict(double[] input) {
        Matrix tmp = Matrix.fromArray(input);

        for (Layer layer : layers) {
            tmp = Matrix.multiply(layer.weight, tmp);
            tmp.addBias(layer.bias);
            tmp.sigmoid();
        }

        return Matrix.toArray(tmp);
    }

    public List<Double> learn(double[] inputNodes, double[] expectedOutputNodes) {
        Matrix input = Matrix.fromArray(inputNodes);

        // forward propagate and add results to list
        List<Matrix> steps = new ArrayList<>();
        Matrix tmp = input;
        for (Layer layer : layers) {
            tmp = Matrix.multiply(layer.weight, tmp);
            tmp.addBias(layer.bias);
            tmp.sigmoid();
            steps.add(tmp);
        }

        Matrix target = (expectedOutputNodes == null) ? tmp: Matrix.fromArray(expectedOutputNodes);

        // backward propagate to adjust weights
        Matrix error = null;
        for (int i = steps.size()-1; i >= 0; i--) {
            if (error == null) {
                error = Matrix.subtract(target, steps.get(steps.size()-1));
            } else {
                error = Matrix.multiply(Matrix.transponse(layers.get(i+1).weight), error);
            }
            Matrix gradient = steps.get(i).dsigmoid();
            gradient.multiplyElementwise(error);
            gradient.multiply(learningRate);
            Matrix delta = Matrix.multiply(gradient, Matrix.transponse((i == 0) ? input : steps.get(i-1)));
            layers.get(i).weight.add(delta);
            layers.get(i).bias.addBias(gradient);
        }

        return Matrix.toArray(tmp);
    }

    // train with sample data in multiple test rounds
    public void train(double[][] inputSet, double[][] expectedOutputSet, int rounds) {
        for (int i = 0; i < rounds; i++) {
            int sampleIndex = (int) (Math.random() * inputSet.length);
            learn(inputSet[sampleIndex], expectedOutputSet[sampleIndex]);
        }
    }

    public static NeuralNetwork merge(NeuralNetwork a, NeuralNetwork b) {
        for (int i = 0; i < a.layers.size(); i++) {
            a.layers.get(i).weight = Matrix.merge(a.layers.get(i).weight, b.layers.get(i).weight);
            a.layers.get(i).bias = Matrix.merge(a.layers.get(i).bias, b.layers.get(i).bias);
        }
        return a;
    }

    public NeuralNetwork clone() {
        NeuralNetwork net = new NeuralNetwork(layers);
        net.randomize(0.1);
        return net;
    }

    private void randomize(double factor) {
        for (Layer layer : layers) {
            layer.weight.randomize(factor);
            layer.bias.randomize(factor);
        }
    }

}
