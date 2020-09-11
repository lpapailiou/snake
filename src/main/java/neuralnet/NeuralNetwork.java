package neuralnet;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {

    List<Layer> layers = new ArrayList<>();

    double learningRate = 0.1;

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


    // forward pass
    public List<Double> predict(double[] input) {
        Matrix tmp = MatrixUtil.fromArray(input);

        for (Layer layer : layers) {
            tmp = MatrixUtil.multiply(layer.weight, tmp);
            tmp.addBias(layer.bias);
            tmp.sigmoid();
        }

        return MatrixUtil.toArray(tmp);
    }

    public void train(double[] x, double[] y) {
        Matrix input = MatrixUtil.fromArray(x);
        Matrix target = MatrixUtil.fromArray(y);
/*
        List<Matrix> steps = new ArrayList<>();
        Matrix tmp;
        for (Layer layer : layers) {
            tmp = MatrixUtil.multiply(layer.weight, tmp);
            tmp.addBias(layer.bias);
            tmp.sigmoid();
            steps.add(0, tmp);
        }*/

        // forward propagate
        Matrix hidden = MatrixUtil.multiply(layers.get(0).weight, input);
        hidden.addBias(layers.get(0).bias);
        hidden.sigmoid();
        Matrix output = MatrixUtil.multiply(layers.get(1).weight, hidden);
        output.addBias(layers.get(1).bias);
        output.sigmoid();



        Matrix error = MatrixUtil.subtract(target, output);
        Matrix gradient = output.dsigmoid();
        gradient.multiplyElementwise(error);
        gradient.multiply(learningRate);

        Matrix hiddenT = MatrixUtil.transponse(hidden);
        Matrix whoDelta = MatrixUtil.multiply(gradient, hiddenT);

        layers.get(1).weight.add(whoDelta);
        layers.get(1).bias.addBias(gradient);

        Matrix whoT = MatrixUtil.transponse(layers.get(1).weight);
        Matrix hiddenErr = MatrixUtil.multiply(whoT, error);

        Matrix hGradient = hidden.dsigmoid();
        hGradient.multiplyElementwise(hiddenErr);
        hGradient.multiply(learningRate);

        Matrix iT = MatrixUtil.transponse(input);
        Matrix wDelta = MatrixUtil.multiply(hGradient, iT);

        layers.get(0).weight.add(wDelta);
        layers.get(0).bias.addBias(hGradient);
    }

    public void fit(double[][] x, double[][] y, int rounds) {
        for (int i = 0; i < rounds; i++) {
            int sample = (int) (Math.random() * x.length);
            train(x[sample], y[sample]);
        }
    }


}
