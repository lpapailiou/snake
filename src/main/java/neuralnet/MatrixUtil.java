package neuralnet;

import java.util.ArrayList;
import java.util.List;

public class MatrixUtil {

    public static Matrix add(Matrix m, double scaler) {
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                m.data[i][j] += scaler;
            }
        }
        return m;
    }

    public static Matrix add(Matrix a, Matrix b) {
        if (a.cols != b.cols || a.rows != b.rows) {
            throw new IllegalArgumentException("wrong input matrix dimensions!");
        }
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                a.data[i][j] += b.data[i][j];
            }
        }
        return a;
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

    public static Matrix transponse(Matrix m) {
        Matrix tmp = new Matrix(m.cols, m.rows);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                tmp.data[j][i] = m.data[i][j];
            }
        }
        return tmp;
    }

    public static Matrix multiply(Matrix m, double scaler) {
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                m.data[i][j] *= scaler;
            }
        }
        return m;
    }

    public static Matrix multiply(Matrix a, Matrix b) {
        //System.out.println("matrix a: " + a.rows + ", " + a.cols);
        //System.out.println("matrix b: " + b.rows + ", " + b.cols);
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

    public static Matrix multiplyElementwise(Matrix a, Matrix b) {
        if (a.cols != b.cols || a.rows != b.rows) {
            throw new IllegalArgumentException("wrong input matrix dimensions!");
        }
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                a.data[i][j] *= b.data[i][j];
            }
        }
        return a;
    }

    public static Matrix sigmoid(Matrix m) {
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                m.data[i][j] = 1/(1 + Math.exp(-m.data[i][j]));
            }
        }
        return m;
    }

    public static Matrix dsigmoid(Matrix m) {
        Matrix tmp = new Matrix(m.rows, m.cols);
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                tmp.data[i][j] = m.data[i][j] * (1 - m.data[i][j]);
            }
        }
        return tmp;
    }

    public static void print(Matrix m) {
        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                System.out.print(m.data[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
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
