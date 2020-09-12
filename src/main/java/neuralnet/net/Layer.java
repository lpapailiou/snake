package neuralnet.net;

class Layer {

    Matrix weight;
    Matrix bias;

    Layer(int m, int n) {
        weight = new Matrix(m, n);
        bias = new Matrix(n, 1);

        // randomize matrices for initial setup
        weight.randomize();
        bias.randomize();
    }
}
