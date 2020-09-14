package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import neuralnet.InputNode;
import util.ColorScheme;
import util.Mode;
import util.Setting;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigPanel implements Initializable {

    private GraphicsContext context;
    private int width = 500;
    private int height = 240;
    private int radius = 20;
    private int offset = 0;

    private List<Integer> network = new ArrayList<>(Setting.getSettings().getNetParamsAsList());
    private List<List<NetNode>> nodes = new ArrayList<>();
    private ObservableList<String> layerCount = FXCollections.observableArrayList("0", "1", "2", "3", "4", "5");
    private ObservableList<String> colorList = FXCollections.observableArrayList(Arrays.asList(ColorScheme.values()).stream().map(m -> m.name()).collect(Collectors.toList()));
    private ObservableList<String> modeList = FXCollections.observableArrayList(Arrays.asList(Mode.values()).stream().map(m -> m.name()).collect(Collectors.toList()));
    private int hiddenLayerNodeCount = 4;
    boolean init = true;
    private static ConfigPanel instance;

    @FXML
    private VBox configPanel;

    @FXML
    private TextField boardWithControl;

    @FXML
    private TextField boardHeightControl;

    @FXML
    private ComboBox colorSchemeChooser;

    @FXML
    private ComboBox hiddenLayerCount;

    @FXML
    private TextField hiddenLayer0;

    @FXML
    private TextField hiddenLayer1;

    @FXML
    private TextField hiddenLayer2;

    @FXML
    private TextField hiddenLayer3;

    @FXML
    private TextField hiddenLayer4;

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
    private ComboBox modeChooser;

    @FXML
    private HBox layerConfig;

    @FXML
    private StackPane netConfig;

    @FXML
    private HBox genConfig;

    @FXML
    private Button startButton;

    @FXML
    private Button statisticsButton;

    @FXML
    private Label genCounter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        Canvas canvas = new Canvas(width, height);
        layerVizualiser.getChildren().add(canvas);
        context = canvas.getGraphicsContext2D();
        setUpMainControl();
        setUpModeControl();
        setUpLayerSetter();
        setUpGenerationConfiguration();
        setUpButtons();
    }

    public static ConfigPanel getPanel() {
        return instance;
    }

    public void lockInput(boolean lock) {
        boardWithControl.setDisable(lock);
        boardHeightControl.setDisable(lock);
        colorSchemeChooser.setDisable(lock);
        modeChooser.setDisable(lock);
        hiddenLayerCount.setDisable(lock);
        hiddenLayer0.setDisable(lock);
        hiddenLayer1.setDisable(lock);
        hiddenLayer2.setDisable(lock);
        hiddenLayer3.setDisable(lock);
        hiddenLayer4.setDisable(lock);
        for (int i = 0; i < inputNodeConfig.getChildren().size(); i++) {
            RadioButton box = (RadioButton) inputNodeConfig.getChildren().get(i);
            box.setDisable(lock);
        }
        generationCount.setDisable(lock);
        populationSize.setDisable(lock);
        learningRate.setDisable(lock);
        startButton.setDisable(lock);
        statisticsButton.setDisable(lock);
    }


    public void incGenCounter() {
        int counter = Integer.parseInt(genCounter.getText());
        counter++;
        genCounter.setText(counter+"");
    }

    public void resetGenCounter() {
        genCounter.setText("0");
    }

    private void setUpButtons() {
        startButton.setOnAction(e -> {
            if (init) {
                GamePanel.getPanel().startBot();
                init = false;
            } else {
                GamePanel.getPanel().restart();
            }
        });
    }

    private void setUpModeControl() {
        modeChooser.setItems(modeList);
        modeChooser.getSelectionModel().select(1);
        modeChooser.setOnAction( e -> {
            updateMode();
        });
        updateMode();
    }

    private void updateMode() {
        Mode mode = Mode.valueOf((String) modeChooser.getValue());
        Setting.getSettings().isBot(mode.isBot());
        Setting.getSettings().setBot(mode.getBotTemplate());
        layerConfig.setVisible((mode == Mode.NEURAL_NETWORK));
        netConfig.setVisible((mode == Mode.NEURAL_NETWORK));
        genConfig.setVisible((mode == Mode.NEURAL_NETWORK));
    }

    private void setUpGenerationConfiguration() {
        generationCount.setText(Setting.getSettings().getGenerationCount() + "");
        populationSize.setText(Setting.getSettings().getPopulationSize() + "");
        learningRate.setText(Setting.getSettings().getLearningRate() + "");
        generationCount.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 0 && result <= 1000) {
                    generationCount.setText(result + "");
                    Setting.getSettings().setGenerationCount(result);
                } else {
                    generationCount.setText(oldValue);
                }
            } catch (Exception e) {
                boardWithControl.setText(oldValue);
            }
        });
        populationSize.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 0 && result <= 1000) {
                    populationSize.setText(result + "");
                    Setting.getSettings().setPopulationSize(result);
                } else {
                    populationSize.setText(oldValue);
                }
            } catch (Exception e) {
                populationSize.setText(oldValue);
            }
        });
        learningRate.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                double result = Double.parseDouble(newValue);
                if (result >= 0 && result <= 1) {
                    learningRate.setText(result + "");
                    Setting.getSettings().setLearningRate(result);
                } else {
                    learningRate.setText(oldValue);
                }
            } catch (Exception e) {
                learningRate.setText(oldValue);
            }
        });
    }

    private void setUpMainControl() {
        boardWithControl.setText(Setting.getSettings().getBoardWidth() + "");
        boardHeightControl.setText(Setting.getSettings().getBoardHeight() + "");
        boardWithControl.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 1 && result <= 200) {
                    boardWithControl.setText(result + "");
                    Setting.getSettings().setBoardWidth(result);
                    GamePanel.getPanel().setDimensions();
                } else {
                    boardWithControl.setText(oldValue);
                }
            } catch (Exception e) {
                boardWithControl.setText(oldValue);
            }
        });
        boardHeightControl.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 1 && result <= 200) {
                    boardHeightControl.setText(result + "");
                    Setting.getSettings().setBoardHeight(result);
                    GamePanel.getPanel().setDimensions();
                } else {
                    boardHeightControl.setText(oldValue);
                }
            } catch (Exception e) {
                boardHeightControl.setText(oldValue);
            }
        });

        colorSchemeChooser.setItems(colorList);
        colorSchemeChooser.getSelectionModel().select(0);
        colorSchemeChooser.setOnAction( e -> {
            updateColorScheme();
        });
    }

    private void updateColorScheme() {
        List<String> cssList = Arrays.asList(ColorScheme.values()).stream().map(s -> s.getCss()).collect(Collectors.toList());
        for (Object str : cssList) {
            String sheet = (String) str;
            configPanel.getScene().getStylesheets().removeIf(s -> s.matches(Driver.class.getClassLoader().getResource(sheet).toExternalForm()));
        }
        String selection = colorSchemeChooser.getValue().toString();
        ColorScheme scheme = ColorScheme.valueOf(selection);
        configPanel.getScene().getStylesheets().remove(Setting.getSettings().getColorScheme().getCss());
        Setting.getSettings().setColorScheme(scheme);
        GamePanel.getPanel().paint();
        configPanel.getScene().setFill(Setting.getSettings().getColorScheme().getBackground());
        configPanel.getScene().getStylesheets().add(Driver.class.getClassLoader().getResource(scheme.getCss()).toExternalForm());
        updateNetwork();
    }

    private void setUpLayerSetter() {
        if (network.size() > 1) {
            hiddenLayer0.setText(network.get(1) + "");
        }
        if (network.size() > 2) {
            hiddenLayer1.setText(network.get(2) + "");
        }
        if (network.size() > 3) {
            hiddenLayer2.setText(network.get(3) + "");
        }
        if (network.size() > 4) {
            hiddenLayer3.setText(network.get(4) + "");
        }
        if (network.size() > 5) {
            hiddenLayer4.setText(network.get(5) + "");
        }
        hiddenLayer0.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 0 && result <= 20) {
                    hiddenLayer0.setText(result + "");
                    updateNetwork();
                } else {
                    hiddenLayer0.setText(oldValue);
                }
            } catch (Exception e) {
                hiddenLayer0.setText(oldValue);
            }
        });
        hiddenLayer1.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 0 && result <= 20) {
                    hiddenLayer1.setText(result + "");
                    updateNetwork();
                } else {
                    hiddenLayer1.setText(oldValue);
                }
            } catch (Exception e) {
                hiddenLayer1.setText(oldValue);
            }
        });
        hiddenLayer2.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 0 && result <= 20) {
                    hiddenLayer2.setText(result + "");
                    updateNetwork();
                } else {
                    hiddenLayer2.setText(oldValue);
                }
            } catch (Exception e) {
                hiddenLayer2.setText(oldValue);
            }
        });
        hiddenLayer3.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 0 && result <= 20) {
                    hiddenLayer3.setText(result + "");
                    updateNetwork();
                } else {
                    hiddenLayer3.setText(oldValue);
                }
            } catch (Exception e) {
                hiddenLayer3.setText(oldValue);
            }
        });
        hiddenLayer4.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                int result = Integer.parseInt(newValue);
                if (result > 0 && result <= 20) {
                    hiddenLayer4.setText(result + "");
                    updateNetwork();
                } else {
                    hiddenLayer4.setText(oldValue);
                }
            } catch (Exception e) {
                hiddenLayer4.setText(oldValue);
            }
        });
        hiddenLayerCount.setItems(layerCount);
        hiddenLayerCount.getSelectionModel().select((Setting.getSettings().getNetParamsAsList().size()-2));
        hiddenLayerCount.setOnAction( e -> {
            updateComboBox();
        });
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
                    Setting.getSettings().removeNodeSelectionNode(index);;
                }
                int[] netParams = Setting.getSettings().getNetParams();
                int activeNodes = (int) nodes.get(0).stream().filter(n -> n.active).count();
                netParams[0] = activeNodes;
                Setting.getSettings().setNetParams(netParams);
                paintNetwork();
            });
        }
    }

    private void updateNetwork() {
        int first = 10;
        int hidden0 = Integer.parseInt(hiddenLayer0.getText());
        int hidden1 = Integer.parseInt(hiddenLayer1.getText());
        int hidden2 = Integer.parseInt(hiddenLayer2.getText());
        int hidden3 = Integer.parseInt(hiddenLayer3.getText());
        int hidden4 = Integer.parseInt(hiddenLayer4.getText());
        int last = 4;
        List<Integer> newNet = new ArrayList<>();
        newNet.add(first);
        if (hidden0 > 0 && hiddenLayer0.isVisible()) {
            newNet.add(hidden0);
        }
        if (hidden1 > 0 && hiddenLayer1.isVisible()) {
            newNet.add(hidden1);
        }
        if (hidden2 > 0 && hiddenLayer2.isVisible()) {
            newNet.add(hidden2);
        }
        if (hidden3 > 0 && hiddenLayer3.isVisible()) {
            newNet.add(hidden3);
        }
        if (hidden4 > 0 && hiddenLayer4.isVisible()) {
            newNet.add(hidden4);
        }
        newNet.add(last);
        network = newNet;
        updateNodes();
        int[] params = new int[newNet.size()];
        for (int i = 0; i < newNet.size(); i++) {
            params[i] = newNet.get(i);
        }
        //int inputLayerCount = (int) Arrays.asList(nodes.get(0)).stream().filter(n -> n.get(0).active).count();
        //System.out.println("input laayer count: "+inputLayerCount);
        //params[0] = inputLayerCount;
        Setting.getSettings().setNetParams(params);
        paintNetwork();
    }

    private void updateComboBox() {
        int selection = Integer.parseInt(hiddenLayerCount.getValue().toString());
        switch (selection) {
            case 0:
                hiddenLayer0.setVisible(false);
                hiddenLayer1.setVisible(false);
                hiddenLayer2.setVisible(false);
                hiddenLayer3.setVisible(false);
                hiddenLayer4.setVisible(false);
                break;
            case 1:
                if (!hiddenLayer0.isVisible()) {
                    hiddenLayer0.setText(hiddenLayerNodeCount + "");
                    hiddenLayer0.setVisible(true);
                }
                hiddenLayer1.setVisible(false);
                hiddenLayer2.setVisible(false);
                hiddenLayer3.setVisible(false);
                hiddenLayer4.setVisible(false);
                break;
            case 2:
                if (!hiddenLayer0.isVisible()) {
                    hiddenLayer0.setText(hiddenLayerNodeCount + "");
                    hiddenLayer0.setVisible(true);
                }
                if (!hiddenLayer1.isVisible()) {
                    hiddenLayer1.setText(hiddenLayerNodeCount + "");
                    hiddenLayer1.setVisible(true);
                }
                hiddenLayer2.setVisible(false);
                hiddenLayer3.setVisible(false);
                hiddenLayer4.setVisible(false);
                break;
            case 3:
                if (!hiddenLayer0.isVisible()) {
                    hiddenLayer0.setText(hiddenLayerNodeCount + "");
                    hiddenLayer0.setVisible(true);
                }
                if (!hiddenLayer1.isVisible()) {
                    hiddenLayer1.setText(hiddenLayerNodeCount + "");
                    hiddenLayer1.setVisible(true);
                }
                if (!hiddenLayer2.isVisible()) {
                    hiddenLayer2.setText(hiddenLayerNodeCount + "");
                    hiddenLayer2.setVisible(true);
                }
                hiddenLayer3.setVisible(false);
                hiddenLayer4.setVisible(false);
                break;
            case 4:
                if (!hiddenLayer0.isVisible()) {
                    hiddenLayer0.setText(hiddenLayerNodeCount + "");
                    hiddenLayer0.setVisible(true);
                }
                if (!hiddenLayer1.isVisible()) {
                    hiddenLayer1.setText(hiddenLayerNodeCount + "");
                    hiddenLayer1.setVisible(true);
                }
                if (!hiddenLayer2.isVisible()) {
                    hiddenLayer2.setText(hiddenLayerNodeCount + "");
                    hiddenLayer2.setVisible(true);
                }
                if (!hiddenLayer3.isVisible()) {
                    hiddenLayer3.setText(hiddenLayerNodeCount + "");
                    hiddenLayer3.setVisible(true);
                }
                hiddenLayer4.setVisible(false);
                break;
            case 5:
                if (!hiddenLayer0.isVisible()) {
                    hiddenLayer0.setText(hiddenLayerNodeCount + "");
                    hiddenLayer0.setVisible(true);
                }
                if (!hiddenLayer1.isVisible()) {
                    hiddenLayer1.setText(hiddenLayerNodeCount + "");
                    hiddenLayer1.setVisible(true);
                }
                if (!hiddenLayer2.isVisible()) {
                    hiddenLayer2.setText(hiddenLayerNodeCount + "");
                    hiddenLayer2.setVisible(true);
                }
                if (!hiddenLayer3.isVisible()) {
                    hiddenLayer3.setText(hiddenLayerNodeCount + "");
                    hiddenLayer3.setVisible(true);
                }
                if (!hiddenLayer4.isVisible()) {
                    hiddenLayer4.setText(hiddenLayerNodeCount + "");
                    hiddenLayer4.setVisible(true);
                }
                break;
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
        context.fillOval(x, y, radius, radius);
    }

    private void paintLine(NetNode a, NetNode b) {
        context.setStroke(Setting.getSettings().getColorScheme().getBackgroundFrame().darker());
        context.setLineWidth(2);
        context.strokeLine(a.x+(radius/2), a.y+(radius/2), b.x+(radius/2), b.y+(radius/2));
    }


    private void paintBackground() {
        context.clearRect(0, 0, width, height);
        context.setFill(Setting.getSettings().getColorScheme().getBackground());
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
