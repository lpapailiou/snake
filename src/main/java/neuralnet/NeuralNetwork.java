package neuralnet;

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
    NeuralNetwork(int... layerParams) {
        if (layerParams.length < 2) {
            throw new IllegalArgumentException("enter at least two parameters to create neural network!");
        }
        for (int i = 1; i < layerParams.length; i++) {
            layers.add(new Layer(layerParams[i], layerParams[i-1]));
        }
    }

    // forward pass
    List<Double> predict(double[] input) {
        Matrix tmp = Matrix.fromArray(input);

        for (Layer layer : layers) {
            tmp = Matrix.multiply(layer.weight, tmp);
            tmp.addBias(layer.bias);
            tmp.sigmoid();
        }

        return Matrix.toArray(tmp);
    }

    private void learn(double[] inputNodes, double[] expectedOutputNodes) {
        Matrix input = Matrix.fromArray(inputNodes);
        Matrix target = Matrix.fromArray(expectedOutputNodes);

        // forward propagate and add results to list
        List<Matrix> steps = new ArrayList<>();
        Matrix tmp = input;
        for (Layer layer : layers) {
            tmp = Matrix.multiply(layer.weight, tmp);
            tmp.addBias(layer.bias);
            tmp.sigmoid();
            steps.add(tmp);
        }

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

    }

    // train with sample data in multiple test rounds
    void train(double[][] inputSet, double[][] expectedOutputSet, int rounds) {
        for (int i = 0; i < rounds; i++) {
            int sampleIndex = (int) (Math.random() * inputSet.length);
            learn(inputSet[sampleIndex], expectedOutputSet[sampleIndex]);
        }
    }

}
