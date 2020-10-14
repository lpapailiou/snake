package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import netadapter.InputNode;
import util.Direction;
import util.Mode;
import util.Setting;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class NeuralNetConfigPanel implements Initializable {

    private GraphicsContext context;
    private int width = 500;
    private int height = 288;
    private double correctionOffset = 1.5;
    private int radius = 16;

    private List<Integer> network = new ArrayList<>(Setting.getSettings().getNetParamsAsList());
    private List<List<NetNode>> nodes = new ArrayList<>();
    private ObservableList<String> layerCount = FXCollections.observableArrayList("0", "1", "2", "3", "4", "5");

    private boolean init = true;
    private static NeuralNetConfigPanel instance;

    @FXML
    private VBox configPanel;

    @FXML
    private ComboBox<String> hiddenLayerCount;

    @FXML
    private Pane layerVizualiser;

    @FXML
    private VBox inputNodeConfig;

    @FXML
    private TextField generationCount;

    @FXML
    private TextField populationSize;

    @FXML
    private TextField learningRate;

    @FXML
    private HBox layerConfig;

    @FXML
    private StackPane netConfig;

    @FXML
    private HBox genConfig;

    @FXML
    private Label genCounter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        Canvas canvas = new Canvas(width, height);
        layerVizualiser.getChildren().add(canvas);
        context = canvas.getGraphicsContext2D();
        setUpLayerSetter();
        setUpGenerationConfiguration();
    }

    public static NeuralNetConfigPanel getPanel() {
        return instance;
    }

    void lockInput(boolean lock) {
        for (Node node : layerConfig.getChildren()) {
            node.setDisable(lock);
        }
        for (int i = 0; i < inputNodeConfig.getChildren().size(); i++) {
            RadioButton box = (RadioButton) inputNodeConfig.getChildren().get(i);
            box.setDisable(lock);
        }
        generationCount.setDisable(lock);
        populationSize.setDisable(lock);
        learningRate.setDisable(lock);
    }

    public void incGenCounter() {
        int counter = Integer.parseInt(genCounter.getText());
        counter++;
        genCounter.setText(counter+"");
    }

    public void resetGenCounter() {
        genCounter.setText("0");
        paintNetwork();
    }

    public void updateMode(Mode mode) {
        layerConfig.setVisible((mode == Mode.NEURAL_NETWORK));
        netConfig.setVisible((mode == Mode.NEURAL_NETWORK));
        genConfig.setVisible((mode == Mode.NEURAL_NETWORK));
    }

    private boolean configureTextField(TextField field, int min, int max, String newValue, String oldValue) {
        try {
            int result = Integer.parseInt(newValue);
            if (result >= min && result <= max) {
                field.setText(result + "");
                return true;
            } else {
                field.setText(oldValue);
            }
        } catch (Exception e) {
            field.setText(oldValue);
        }
        return false;
    }

    private boolean configureDoubleTextField(TextField field, int min, int max, String newValue, String oldValue) {
        try {
            double result = Double.parseDouble(newValue);
            if (result >= min && result <= max) {
                field.setText(result + "");
                return true;
            } else {
                field.setText(oldValue);
            }
        } catch (Exception e) {
            field.setText(oldValue);
        }
        return false;
    }

    private void setUpGenerationConfiguration() {
        generationCount.setText(Setting.getSettings().getGenerationCount() + "");
        populationSize.setText(Setting.getSettings().getPopulationSize() + "");
        learningRate.setText(Setting.getSettings().getLearningRate() + "");

        AtomicReference<String> tempGenerations = new AtomicReference<String>(Setting.getSettings().getGenerationCount() + "");
        generationCount.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempGenerations.toString();
            tempGenerations.set(generationCount.getText());
            if (configureTextField(generationCount, 1, 5000, tempGenerations.toString(), previousValue)) {
                Setting.getSettings().setGenerationCount(Integer.parseInt(tempGenerations.toString()));
            }
        });

        AtomicReference<String> tempPopulations = new AtomicReference<String>(Setting.getSettings().getPopulationSize() + "");
        populationSize.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempPopulations.toString();
            tempPopulations.set(populationSize.getText());
            if (configureTextField(populationSize, 1, 5000, tempPopulations.toString(), previousValue)) {
                Setting.getSettings().setPopulationSize(Integer.parseInt(tempPopulations.toString()));
            }
        });

        AtomicReference<String> tempRate = new AtomicReference<String>(Setting.getSettings().getLearningRate() + "");
        learningRate.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempRate.toString();
            tempRate.set(learningRate.getText());
            if (configureDoubleTextField(learningRate, 0, 1, tempRate.toString(), previousValue)) {
                Setting.getSettings().setLearningRate(Double.parseDouble(tempRate.toString()));
            }
        });
    }

    private void setUpLayerSetter() {
        for (int i = 0; i < Setting.getSettings().getNetParams()[0]; i++) {
            RadioButton but = new RadioButton();
            but.setSelected(true);
            inputNodeConfig.getChildren().add(but);
        }

        for (int i = 1; i < layerConfig.getChildren().size(); i++) {
            TextField field = (TextField) layerConfig.getChildren().get(i);
            if (network.size() > i) {
                field.setText(network.get(i)+"");
            }
            field.textProperty().addListener((o, oldValue, newValue) -> {
                if (configureTextField(field, 1, 64, newValue, oldValue)) {
                    updateNetwork();
                }
            });
        }
        hiddenLayerCount.setItems(layerCount);
        hiddenLayerCount.getSelectionModel().select((Setting.getSettings().getNetParamsAsList().size()-2));
        hiddenLayerCount.setOnAction( e -> updateComboBox());
        updateComboBox();
        for (int i = 0; i < inputNodeConfig.getChildren().size(); i++) {
            RadioButton box = (RadioButton) inputNodeConfig.getChildren().get(i);
            box.setTooltip(new Tooltip(InputNode.values()[i].getTooltip()));
            box.setOnAction(e -> {
                if (!box.isSelected()) {
                    int activeNodes = (int) nodes.get(0).stream().filter(n -> n.active).count();
                    if (activeNodes == 1) {
                        box.setSelected(true);
                    }
                }
                int index = inputNodeConfig.getChildren().indexOf(box);
                nodes.get(0).get(index).active = box.isSelected();
                if (box.isSelected()) {
                    Setting.getSettings().addNodeSelectionNode(index);
                } else {
                    Setting.getSettings().removeNodeSelectionNode(index);
                }
                int[] netParams = Setting.getSettings().getNetParams();
                int activeNodes = (int) nodes.get(0).stream().filter(n -> n.active).count();
                netParams[0] = activeNodes;
                Setting.getSettings().setNetParams(netParams);
                paintNetwork();
            });
        }
    }

    public void updateNetwork() {
        int first = Setting.getSettings().getInitialInputNodeCount();
        int last = 4;
        List<Integer> newNet = new ArrayList<>();
        newNet.add(first);
        for (int i = 1; i < layerConfig.getChildren().size(); i++) {
            TextField field = (TextField) layerConfig.getChildren().get(i);
            if (field.isVisible()) {
                newNet.add(Integer.parseInt(field.getText()));
            }
        }
        newNet.add(last);
        network = newNet;
        updateNodes();
        int[] params = new int[newNet.size()];
        for (int i = 0; i < newNet.size(); i++) {
            if (i == 0) {
                params[i] = Setting.getSettings().getNodeSelection().size();
            } else {
                params[i] = newNet.get(i);
            }
        }
        Setting.getSettings().setNetParams(params);
        paintNetwork();
    }

    private void updateComboBox() {
        int selection = Integer.parseInt(hiddenLayerCount.getValue());
        for (int i = 1; i < layerConfig.getChildren().size(); i++) {
            TextField field = (TextField) layerConfig.getChildren().get(i);
            if (i <= selection) {
                if (!field.isVisible()) {
                    int hiddenLayerNodeCount = 4;
                    field.setText(hiddenLayerNodeCount + "");
                    field.setVisible(true);
                }
            } else {
                field.setVisible(false);
            }
        }
        updateNetwork();
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

    public void flashOutput(int flash) {
        for (int i = 0; i < nodes.get(nodes.size()-1).size(); i++) {
            NetNode node = nodes.get(nodes.size()-1).get(i);
            if (i == flash) {
                paintDot(node.x, node.y, radius, Setting.getSettings().getColorScheme().getGoodie());
            } else {
                paintDot(node.x, node.y, radius);
            }
        }
    }

    private void paintDots() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.get(i).size(); j++) {
                NetNode node = nodes.get(i).get(j);
                if (node.active) {
                    paintDot(node.x, node.y, radius);
                    if (i == nodes.size()-1) {
                        context.setLineWidth(0.7);
                        context.setFont(new Font("Courier New", 12));
                        context.strokeText(Direction.values()[j].name(), node.x+radius*0.5, node.y-radius*0.5);
                    }
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
                int offset = 0;
                NetNode node = new NetNode((w * i) + offset, (h * j) + hOffset);
                layer.add(node);
                if (i == 0) {
                    RadioButton box = (RadioButton) inputNodeConfig.getChildren().get(j);
                    if (!box.isSelected()) {
                        node.active = false;
                    }
                }
            }
            nodes.add(layer);
        }
    }

    private void paintDot(int x, int y, int radius) {
        context.setFill(Setting.getSettings().getColorScheme().getBackgroundFrame());
        context.fillOval(x+correctionOffset, y+correctionOffset, radius, radius);
    }

    private void paintDot(int x, int y, int radius, Color color) {
        context.setFill(color);
        context.fillOval(x+correctionOffset, y+correctionOffset, radius, radius);
    }

    private void paintLine(NetNode a, NetNode b) {
        context.setStroke(Setting.getSettings().getColorScheme().getBackgroundFrame().darker());
        context.setLineWidth(2);
        context.strokeLine(a.x+(radius/2)+correctionOffset, a.y+(radius/2)+correctionOffset, b.x+(radius/2)+correctionOffset, b.y+(radius/2)+correctionOffset);
    }


    private void paintBackground() {
        context.clearRect(0, 0, width, height);
        context.setFill(Setting.getSettings().getColorScheme().getBackground());
        context.fillRect(0, 0, width,height);
    }

    private static class NetNode {
        int x;
        int y;
        boolean active = true;
        NetNode(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
