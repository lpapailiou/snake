package neuralnet;

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


    public void subtract(Matrix m) {
        if (cols != m.cols || rows != m.rows) {
            throw new IllegalArgumentException("wrong input matrix dimensions!");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] -= m.data[i][j];
            }
        }
    }

    public void transponse() {
        Matrix tmp = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tmp.data[i][j] = data[i][j];
            }
        }
        data = tmp.data;
        rows = tmp.rows;
        cols = tmp.cols;
    }

    public void multiply(double scaler) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] *= scaler;
            }
        }
    }

    public void multiply(Matrix m) {
        if (cols != m.rows) {
            throw new IllegalArgumentException("wrong input matrix dimensions!");
        }
        Matrix tmp = new Matrix(rows, m.cols);
        for (int i = 0; i < tmp.rows; i++) {
            for (int j = 0; j < tmp.cols; j++) {
                double sum = 0;
                for (int k = 0; k < m.cols; k++) {
                    sum += data[i][k] * m.data[k][j];
                }
                tmp.data[i][j] = sum;
            }
        }
        this.data = tmp.data;
        this.cols = tmp.cols;
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

}
