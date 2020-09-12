package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static util.Setting.*;

public class ConfigPanel implements Initializable {

    GraphicsContext context;
    int layers = 4;
    int width = 400;
    int height = 221;
    int offset = 20;
    int radius = 20;
    List<Integer> network = new ArrayList<>(Arrays.asList(9, 10, 2, 4));

    @FXML
    private Pane layerVizualiser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Canvas canvas = new Canvas(width, height);
        layerVizualiser.getChildren().add(canvas);
        context = canvas.getGraphicsContext2D();
        paintBackground();
        paintLines();
        paintDots();
    }

    private void paintLines() {
        List<List<int[]>> nodes = getNodes();
        for (int i = 1; i < nodes.size(); i++) {
            cross(nodes.get(i), nodes.get(i-1));
        }
    }

    private void cross(List<int[]> layer1, List<int[]> layer2) {
        for (int[] a : layer1) {
            for (int[] b : layer2) {
                paintLine(a, b);
            }
        }
    }

    private void paintLine(int[] a, int[] b) {
        context.setStroke(COLOR_SCHEME.getSnake().darker());
        context.setLineWidth(2);
        context.strokeLine(a[0]+(radius/2), a[1]+(radius/2), b[0]+(radius/2), b[1]+(radius/2));
    }

    private void paintDots() {
        List<List<int[]>> nodes = getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.get(i).size(); j++) {
                paintDot(nodes.get(i).get(j)[0], nodes.get(i).get(j)[1], radius);
            }
        }
    }

    private List<List<int[]>> getNodes() {
        List<List<int[]>> nodes = new ArrayList<>();
        int w = width/network.size();
        for (int i = 0; i < network.size(); i++) {
            List<int[]> layer = new ArrayList<>();
            int h = height/network.get(i);
            int hOffset = (height - ((network.get(i)-1)*h)-offset)/2;
            System.out.println(h + ", " + (network.get(i)*h) + ", " + hOffset);
            for (int j = 0; j < network.get(i); j++) {
                layer.add(new int[] {(w*i)+offset, (h * j)+hOffset});
            }
            nodes.add(layer);
        }
        return nodes;
    }

    private void paintDot(int x, int y, int radius) {
        context.setFill(COLOR_SCHEME.getSnake());
        context.fillOval(x, y, radius, radius);
    }


    private void paintBackground() {
        context.clearRect(0, 0, width, height);
        context.setFill(COLOR_SCHEME.getBackground());
        context.fillRect(0, 0, width,height);
    }
}
