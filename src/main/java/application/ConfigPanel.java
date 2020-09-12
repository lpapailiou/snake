package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    int width = 500;
    int height = 221;
    int offset = 50;
    int radius = 20;
    List<Integer> network = new ArrayList<>(Arrays.asList(9, 10, 3, 7, 4));
    List<List<NetNode>> nodes = new ArrayList<>();

    @FXML
    private Pane layerVizualiser;

    @FXML
    private VBox inputNodeConfig;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Canvas canvas = new Canvas(width, height);
        layerVizualiser.getChildren().add(canvas);
        context = canvas.getGraphicsContext2D();
        updateNodes();
        paintNetwork();

        for (int i = 0; i < inputNodeConfig.getChildren().size(); i++) {
            CheckBox box = (CheckBox) inputNodeConfig.getChildren().get(i);
            box.setOnAction(e -> {
                int index = inputNodeConfig.getChildren().indexOf(box);
                nodes.get(0).get(index).active = box.isSelected();
                paintNetwork();
            });
        }
    }

    private void paintNetwork() {
        paintBackground();
        paintLines();
        paintDots();
    }

    private void paintLines() {
        for (int i = 1; i < nodes.size(); i++) {
            cross(nodes.get(i), nodes.get(i-1));
        }
    }

    private void cross(List<NetNode> layer1, List<NetNode> layer2) {
        for (NetNode a : layer1) {
            for (NetNode b : layer2) {
                if (a.active && b.active) {
                    paintLine(a, b);
                }
            }
        }
    }

    private void paintLine(NetNode a, NetNode b) {
        context.setStroke(COLOR_SCHEME.getSnake().darker());
        context.setLineWidth(2);
        context.strokeLine(a.x+(radius/2), a.y+(radius/2), b.x+(radius/2), b.y+(radius/2));
    }

    private void paintDots() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.get(i).size(); j++) {
                NetNode node = nodes.get(i).get(j);
                if (node.active) {
                    paintDot(node.x, node.y, radius);
                }
            }
        }
    }

    private void updateNodes() {
        nodes.clear();
        int w = width/network.size();
        for (int i = 0; i < network.size(); i++) {
            List<NetNode> layer = new ArrayList<>();
            int h = height/network.get(i);
            int hOffset = (height - ((network.get(i)-1)*h)-20)/2;
            for (int j = 0; j < network.get(i); j++) {
                NetNode node =  new NetNode((w * i) + offset, (h * j) + hOffset);
                layer.add(node);
                if (i == 0) {
                    CheckBox box = (CheckBox) inputNodeConfig.getChildren().get(j);
                    if (!box.isSelected()) {
                        node.active = false;
                    }
                }
            }
            nodes.add(layer);
        }
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

    private class NetNode {
        int x;
        int y;
        boolean active = true;
        NetNode(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
