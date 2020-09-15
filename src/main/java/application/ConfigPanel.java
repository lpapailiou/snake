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
import javafx.scene.text.Text;
import neuralnet.InputNode;
import util.ColorScheme;
import util.Direction;
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

    private List<Integer> network = new ArrayList<>(Setting.getSettings().getNetParamsAsList());
    private List<List<NetNode>> nodes = new ArrayList<>();
    private ObservableList<String> layerCount = FXCollections.observableArrayList("0", "1", "2", "3", "4", "5");
    private ObservableList<String> colorList = FXCollections.observableArrayList(Arrays.stream(ColorScheme.values()).map(Enum::name).collect(Collectors.toList()));
    private ObservableList<String> modeList = FXCollections.observableArrayList(Arrays.stream(Mode.values()).map(Enum::name).collect(Collectors.toList()));
    private boolean init = true;
    private static ConfigPanel instance;

    @FXML
    private VBox configPanel;

    @FXML
    private TextField boardWithControl;

    @FXML
    private TextField boardHeightControl;

    @FXML
    private ComboBox<String> colorSchemeChooser;

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
    private ComboBox<String> modeChooser;

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
        setUpLayerSetter();
        setUpMainControl();

        setUpModeControl();

        setUpGenerationConfiguration();
        setUpButtons();
    }

    public static ConfigPanel getPanel() {
        return instance;
    }

    void lockInput(boolean lock) {
        boardWithControl.setDisable(lock);
        boardHeightControl.setDisable(lock);
        colorSchemeChooser.setDisable(lock);
        modeChooser.setDisable(lock);
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
        paintNetwork();
    }

    private void setUpButtons() {
        startButton.setOnAction(e -> {
            if (init) {
                GamePanel.getPanel().startBot();
                init = false;
            } else {
                GamePanel.restart();
            }
        });
    }

    private void setUpModeControl() {
        modeChooser.setItems(modeList);
        modeChooser.getSelectionModel().select(1);
        modeChooser.setOnAction( e -> updateMode());
        updateMode();
    }

    private void updateMode() {
        Mode mode = Mode.valueOf(modeChooser.getValue());
        Setting.getSettings().isBot(mode.isBot());
        Setting.getSettings().setBot(mode.getBotTemplate());
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
        generationCount.textProperty().addListener((o, oldValue, newValue) -> {
            if (configureTextField(generationCount, 1, 1000, newValue, oldValue)) {
                Setting.getSettings().setGenerationCount(Integer.parseInt(newValue));
            }
        });
        populationSize.textProperty().addListener((o, oldValue, newValue) -> {
            if (configureTextField(populationSize, 1, 2000, newValue, oldValue)) {
                Setting.getSettings().setPopulationSize(Integer.parseInt(newValue));
            }
        });
        learningRate.textProperty().addListener((o, oldValue, newValue) -> {
            if (configureDoubleTextField(learningRate, 0, 1, newValue, oldValue)) {
                Setting.getSettings().setLearningRate(Double.parseDouble(newValue));
            }
        });
    }

    private void setUpMainControl() {
        boardWithControl.setText(Setting.getSettings().getBoardWidth() + "");
        boardHeightControl.setText(Setting.getSettings().getBoardHeight() + "");
        boardWithControl.textProperty().addListener((o, oldValue, newValue) -> {
            if (configureTextField(boardWithControl, 1, 100, newValue, oldValue)) {
                Setting.getSettings().setBoardWidth(Integer.parseInt(newValue));
                GamePanel.getPanel().setDimensions();
            }
        });
        boardHeightControl.textProperty().addListener((o, oldValue, newValue) -> {
            if (configureTextField(boardHeightControl, 1, 100, newValue, oldValue)) {
                Setting.getSettings().setBoardHeight(Integer.parseInt(newValue));
                GamePanel.getPanel().setDimensions();
            }
        });

        colorSchemeChooser.setItems(colorList);
        colorSchemeChooser.getSelectionModel().select(0);
        colorSchemeChooser.setOnAction( e -> updateColorScheme());
    }

    private void updateColorScheme() {
        List<String> cssList = Arrays.stream(ColorScheme.values()).map(ColorScheme::getCss).collect(Collectors.toList());
        for (Object str : cssList) {
            String sheet = (String) str;
            configPanel.getScene().getStylesheets().removeIf(s -> s.matches(Objects.requireNonNull(Driver.class.getClassLoader().getResource(sheet)).toExternalForm()));
        }
        String selection = colorSchemeChooser.getValue();
        ColorScheme scheme = ColorScheme.valueOf(selection);
        configPanel.getScene().getStylesheets().remove(Setting.getSettings().getColorScheme().getCss());
        Setting.getSettings().setColorScheme(scheme);
        GamePanel.getPanel().paint();
        configPanel.getScene().setFill(Setting.getSettings().getColorScheme().getBackground());
        configPanel.getScene().getStylesheets().add(Objects.requireNonNull(Driver.class.getClassLoader().getResource(scheme.getCss())).toExternalForm());
        updateNetwork();
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
                if (configureTextField(field, 1, 20, newValue, oldValue)) {
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

    private void updateNetwork() {
        int first = network.get(0);
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
            params[i] = newNet.get(i);
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
        context.fillOval(x, y, radius, radius);
    }

    private void paintDot(int x, int y, int radius, Color color) {
        context.setFill(color);
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
