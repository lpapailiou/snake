package neuralnet;

import java.util.ArrayList;
import java.util.List;

public class Matrix {

    double[][] data;
    int rows;
    int cols;

    public Matrix(int rows, int cols) {
        data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public Matrix(double[][] input) {
        data = input;
        rows = input.length;
        cols = input[0].length;
    }

    public void randomize() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Math.random() * 2 - 1;
            }
        }
    }

    public void add(double scaler) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] += scaler;
            }
        }
    }

    public void add(Matrix m) {
        if (cols != m.cols || rows != m.rows) {
            throw new IllegalArgumentException("wrong input matrix dimensions for addition!");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] += m.data[i][j];
            }
        }
    }

    public void addBias(Matrix m) {
        if (cols != m.cols) {
            throw new IllegalArgumentException("wrong input matrix dimensions!");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] += m.data[0][j];
            }
        }
    }

    public static Matrix subtract(Matrix a, Matrix b) {
        if (a.cols != b.cols) {
            throw new IllegalArgumentException("wrong input matrix dimensions! " + a.getType() + " vs. " + b.getType());
        }
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                a.data[i][j] -= b.data[i][j];
            }
        }
        return a;
    }

    public void multiply(double scaler) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] *= scaler;
            }
        }
    }

    public void multiplyElementwise(Matrix m) {
        if (cols != m.cols || rows != m.rows) {
            throw new IllegalArgumentException("wrong input matrix dimensions!");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] *= m.data[i][j];
            }
        }
    }

    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.cols != b.rows) {
            throw new IllegalArgumentException("wrong input matrix dimensions for multiplication!");
        }
        Matrix tmp = new Matrix(a.rows, b.cols);
        for (int i = 0; i < tmp.rows; i++) {
            for (int j = 0; j < tmp.cols; j++) {
                double sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                tmp.data[i][j] = sum;
            }
        }
        return tmp;
    }

    public static Matrix transponse(Matrix m) {
        Matrix tmp = new Matrix(m.cols, m.rows);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                tmp.data[j][i] = m.data[i][j];
            }
        }
        return tmp;
    }

    public void sigmoid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = 1/(1 + Math.exp(-data[i][j]));
            }
        }
    }

    public Matrix dsigmoid() {
        Matrix tmp = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tmp.data[i][j] = data[i][j] * (1 - data[i][j]);
            }
        }
        return tmp;
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(data[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public String getType() {
        return "(" + rows + ", " + cols + ")";
    }

    public static Matrix fromArray(double[] arr) {
        Matrix tmp = new Matrix(arr.length, 1);
        for (int i = 0; i < arr.length; i++) {
            tmp.data[i][0] = arr[i];
        }
        return tmp;
    }

    public static List<Double> toArray(Matrix m) {
        List<Double> tmp = new ArrayList<>();
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                tmp.add(m.data[i][j]);
            }
        }
        return tmp;
    }

}
