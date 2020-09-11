package neuralnet;

public class Layer {

    Matrix weight;
    Matrix bias;

    public Layer(int m, int n) {
        weight = new Matrix(m, n);
        bias = new Matrix(n, 1);

        weight.randomize();
        bias.randomize();
        //weight.print();

        //System.out.println("LAYER CREATED: ");
        //weight.print();
    }
}
