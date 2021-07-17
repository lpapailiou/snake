package ui;

import ai.netadapter.InputNode;
import main.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import nn.neuralnet.NeuralNetwork;
import nn.ui.NNGraph;
import ui.util.ControlsAnimator;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigController implements Initializable {

    @FXML
    private HBox baseControls;

    @FXML
    private TextField boardWithControl;

    @FXML
    private TextField boardHeightControl;

    @FXML
    private ComboBox<String> themeSelector;

    @FXML
    private ComboBox<String> modeSelector;

    @FXML
    private HBox neuralNetworkControls;

    @FXML
    private ComboBox<String> hiddenLayerCount;

    @FXML
    private HBox hiddenLayerControls;

    @FXML
    private VBox inputNodeConfiguration;

    @FXML
    private Canvas layerPane;

    @FXML
    private TextField generationControl;

    @FXML
    private TextField populationControl;

    @FXML
    private TextField randomizationControl;

    @FXML
    private HBox statisticControls;

    @FXML
    private Button statisticsButton;

    @FXML
    private HBox actionControls;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    private ApplicationController applicationController;
    private GameController gameController;
    private Config config = Config.getInstance();

    private NNGraph visualizer;
    private int[] inactiveIndexArr = new int[]{};
    private GraphicsContext context;
    private ObservableList<String> colorList = FXCollections.observableArrayList(Arrays.stream(
            Theme.values()).map(Enum::name).collect(Collectors.toList()));
    private ObservableList<String> modeList = FXCollections.observableArrayList(
            Arrays.stream(Mode.values()).map(Mode::getLabel).collect(Collectors.toList()));
    private ObservableList<String> layerCount = FXCollections
            .observableArrayList("0", "1", "2", "3", "4", "5");
    private ControlsAnimator controlsAnimator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        context = layerPane.getGraphicsContext2D();
        controlsAnimator = new ControlsAnimator(modeSelector, baseControls,
                actionControls, neuralNetworkControls, statisticControls);
        visualizer = new NNGraph(context);
        visualizer.setNeuralNetwork(new NeuralNetwork(config.getLayerConfiguration()));
        visualizer.setOutputNodeLabels(Direction.getLabels());
        visualizer.setColorPalette(config.getTheme().getNnColorPalette());
        initializeBaseControls();
        initializeLayerControls();
        initializeGenerationControls();
        initializeButtons();
        Platform.runLater(() -> boardWithControl.getParent().requestFocus());
    }

    private void openStatistics() {
        Runtime rt = Runtime.getRuntime();
        String url = "http://localhost:8050/Dashboard.html";
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                rt.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {

                String[] supportedBrowsers = {"chromium", "google-chrome", "epiphany", "firefox", "mozilla",
                        "konqueror", "netscape", "opera", "links", "lynx"};

                String browserOpeningCommandString = Arrays.stream(supportedBrowsers)
                        .map(browser -> String.format("%s \"%s\"", browser, url))
                        .collect(Collectors.joining(" || "));
                rt.exec(new String[]{"sh", "-c", browserOpeningCommandString});
            }

        } catch (IOException e) {
            Logger.getLogger("config controller logger")
                    .log(Level.WARNING, "Browser could not be opened.", e);
        }
    }

    private void updateTheme() {
        Arrays.stream(Theme.values())
                .map(Theme::getCss)
                .forEach(cssFile -> this.boardWithControl.getScene().getStylesheets().removeIf(s -> s.matches(
                        Objects.requireNonNull(Main.class.getClassLoader().getResource(cssFile))
                                .toExternalForm())));
        Scene scene = this.boardWithControl.getScene();
        Theme theme = Theme.valueOf(themeSelector.getValue());
        scene.getStylesheets().remove(config.getTheme().getCss());
      config.setTheme(theme);
        gameController.resetGameDisplay();
        scene.setFill(theme.getBackgroundColor());
        scene.getStylesheets().add(
                Objects.requireNonNull(Main.class.getClassLoader().getResource(theme.getCss()))
                        .toExternalForm());
        visualizer.setColorPalette(theme.getNnColorPalette());
    }

    private void updateMode() {
        Mode newMode = Arrays.stream(Mode.values())
                .filter(e -> e.getLabel().equals(modeSelector.getValue())).findFirst().get();
        Mode oldMode = config.getMode();
        if (applicationController != null) {
            Scene scene = applicationController.getScene();
            controlsAnimator.animateModeTransition(newMode, oldMode, scene);
        }

        neuralNetworkControls.setVisible(newMode == Mode.NEURAL_NETWORK);
        statisticControls.setVisible(newMode == Mode.NEURAL_NETWORK);
        config.setMode(newMode);
    }

    private void updateHiddenLayerSelection() {
        int selection = Integer.parseInt(hiddenLayerCount.getValue());
        for (int i = 0; i < hiddenLayerControls.getChildren().size(); i++) {
            TextField field = (TextField) hiddenLayerControls.getChildren().get(i);
            if (i < selection) {
                if (!field.isVisible()) {
                    int hiddenLayerNodeCount = 4;
                    field.setText(hiddenLayerNodeCount + "");
                    field.setVisible(true);
                }
            } else {
                field.setVisible(false);
            }
        }
        updateNetworkParameter();
    }

    private void updateNetworkParameter() {

        int[] currentParams = config.getLayerConfiguration();
        int nodes = (int) hiddenLayerControls.getChildren().stream().filter(Node::isVisible).count();
        int[] network = new int[nodes + 2];
        network[0] = currentParams[0];
        network[network.length - 1] = currentParams[currentParams.length - 1];
        Set invisibleNodes = new HashSet();
        int index = 1;
        for (Node node : hiddenLayerControls.getChildren()) {
            TextField field = (TextField) node;
            if (field.isVisible()) {
                network[index] = Integer.parseInt(field.getText());
                index++;
            }
        }
        config.setLayerConfiguration(network);
        visualizer.setNeuralNetwork(new NeuralNetwork(network));
        updateNodeCount();
    }

    private void updateNodeCount() {
        Set<Integer> activeIndexes = config.getInputNodeSelection();
        Stream<Integer> stream = Stream.iterate(0, n -> n + 1).limit(12);
        Set<Integer> intList = stream.filter(n -> !activeIndexes.contains(n)).collect(Collectors.toSet());
        visualizer.setGraphInputNodeCount(12, intList);
    }

    void listenToNeuralNetwork(NeuralNetwork neuralNetwork) {
        visualizer.setNeuralNetwork(neuralNetwork);
        updateNodeCount();
    }

    private void initializeBaseControls() {
        boardWithControl.setText(config.getBoardWidth() + "");
        boardHeightControl.setText(config.getBoardHeight() + "");
        AtomicReference<String> tempWidth = new AtomicReference<>(config.getBoardWidth() + "");
        boardWithControl.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempWidth.toString();
            tempWidth.set(boardWithControl.getText());
            if (configureTextField(boardWithControl, 4, 100, tempWidth.toString(),
                    previousValue)) {
                if (!tempWidth.toString().equals(previousValue)) {
                    config.setBoardWidth(Integer.parseInt(tempWidth.toString()));
                    gameController.resetGameDisplay();
                }
            } else {
                showPopupMessage("min: 4, max: 100", boardWithControl);
            }
        });

        AtomicReference<String> tempHeight = new AtomicReference<>(config.getBoardHeight() + "");
        boardHeightControl.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempHeight.toString();
            tempHeight.set(boardHeightControl.getText());
            if (configureTextField(boardHeightControl, 4, 100, tempHeight.toString(),
                    previousValue)) {
                if (!tempHeight.toString().equals(previousValue)) {
                    config.setBoardHeight(Integer.parseInt(tempHeight.toString()));
                    gameController.resetGameDisplay();
                }
            } else {
                showPopupMessage("min: 4, max: 100", boardHeightControl);
            }
        });

        themeSelector.setItems(colorList);
        themeSelector.getSelectionModel().select(config.getTheme().ordinal());
        themeSelector.setOnAction(e -> updateTheme());

        modeSelector.setItems(modeList);
        modeSelector.getSelectionModel().select(config.getMode().ordinal());
        modeSelector.setOnAction(e -> updateMode());

        updateMode();

        neuralNetworkControls.managedProperty().bind(neuralNetworkControls.visibleProperty());
        statisticControls.managedProperty().bind(statisticControls.visibleProperty());
    }

    private void initializeLayerControls() {
        hiddenLayerCount.setItems(layerCount);
        hiddenLayerCount.getSelectionModel()
                .select((config.getLayerConfigurationAsList().size() - 2));
        hiddenLayerCount.setOnAction(e -> updateHiddenLayerSelection());

        for (int i = 0; i < config.getLayerConfiguration()[0]; i++) {
            RadioButton button = new RadioButton();
            button.setSelected(true);
            inputNodeConfiguration.getChildren().add(button);
        }

        for (int i = 0; i < hiddenLayerControls.getChildren().size(); i++) {
            TextField field = (TextField) hiddenLayerControls.getChildren().get(i);
            List<Integer> layer = config.getLayerConfigurationAsList();
            if (i < layer.size() - 2) {
                field.setText(layer.get(i + 1) + "");
            } else {
                field.setVisible(false);
            }
            String nodeCount = (config.getLayerConfiguration().length > i + 1) ?
                    config.getLayerConfiguration()[i + 1] + "" : "4";
            AtomicReference<String> tempValue = new AtomicReference<>(nodeCount);
            field.focusedProperty().addListener((o, oldValue, newValue) -> {
                String previousValue = tempValue.toString();
                tempValue.set(field.getText());
                if (configureTextField(field, 1, 64, tempValue.toString(), previousValue)) {
                    if (!tempValue.toString().equals(previousValue)) {
                        updateNetworkParameter();
                    }
                } else {
                    showPopupMessage("min: 1, max: 64", field);
                }
            });
        }
        for (int i = 0; i < inputNodeConfiguration.getChildren().size(); i++) {
            RadioButton box = (RadioButton) inputNodeConfiguration.getChildren().get(i);
            box.setTooltip(new Tooltip(InputNode.values()[i].getTooltip()));
            box.selectedProperty().addListener(e -> {
                if (!box.isSelected()) {
                    int activeNodes = (int) inputNodeConfiguration.getChildren().stream()
                            .filter(n -> ((RadioButton) n).isSelected()).count();
                    if (activeNodes == 0) {
                        box.setSelected(true);
                        return;
                    }
                }

                int index = inputNodeConfiguration.getChildren().indexOf(box);
                if (box.isSelected()) {
                  config.addInputNode(index);
                } else {
                  config.removeInputNodeFromSelection(index);
                }
                int[] netParams = config.getLayerConfiguration();
                int activeNodes = (int) inputNodeConfiguration.getChildren().stream()
                        .filter(n -> ((RadioButton) n).isSelected()).count();
                netParams[0] = activeNodes;
                config.setLayerConfiguration(netParams);

                Set<Integer> selectedNodes = config.getInputNodeSelection();
                inactiveIndexArr = new int[12-selectedNodes.size()];
                int indx = 0;
                for (int k = 0; k <12; k++) {
                    if (!selectedNodes.contains(k)) {
                        inactiveIndexArr[indx] = k;
                        indx++;
                    }
                }
                visualizer.setNeuralNetwork(new NeuralNetwork(config.getLayerConfiguration()));
                updateNodeCount();
            });
        }
    }

    private void initializeGenerationControls() {
        generationControl.setText(config.getGenerationCount() + "");
        populationControl.setText(config.getPopulationSize() + "");
        randomizationControl.setText(config.getRandomizationRate() + "");

        AtomicReference<String> tempGenerations = new AtomicReference<>(
                config.getGenerationCount() + "");
        generationControl.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempGenerations.toString();
            tempGenerations.set(generationControl.getText());
            if (configureTextField(generationControl, 1, 5000, tempGenerations.toString(),
                    previousValue)) {
              config.setGenerationCount(Integer.parseInt(tempGenerations.toString()));
            } else {
                showPopupMessage("min: 1, max: 5000", generationControl);
            }
        });

        AtomicReference<String> tempPopulations = new AtomicReference<>(
                config.getPopulationSize() + "");
        populationControl.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempPopulations.toString();
            tempPopulations.set(populationControl.getText());
            if (configureTextField(populationControl, 1, 5000, tempPopulations.toString(),
                    previousValue)) {
              config.setPopulationSize(Integer.parseInt(tempPopulations.toString()));
            } else {
                showPopupMessage("min: 1, max: 5000", populationControl);
            }
        });

        AtomicReference<String> tempRate = new AtomicReference<>(
                config.getRandomizationRate() + "");
        randomizationControl.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempRate.toString();
            tempRate.set(randomizationControl.getText());
            if (configureDoubleTextField(randomizationControl, 0, 1, tempRate.toString(),
                    previousValue)) {
              config.setRandomizationRate(Double.parseDouble(tempRate.toString()));
            } else {
                showPopupMessage("min: 0, max: 1", randomizationControl);
            }
        });
    }

    private void initializeButtons() {
        statisticsButton.setOnAction(e -> openStatistics());
        startButton.setOnAction(e -> applicationController.launchGame());
        stopButton.setOnAction(e -> applicationController.stopGame());
        stopButton.setDisable(true);
    }

    void setDisable(boolean disable) {
        baseControls.setDisable(disable);
        neuralNetworkControls.setDisable(disable);
        startButton.setDisable(disable);
        stopButton.setDisable(!disable);

        if (!disable) {
            Platform.runLater(() -> boardWithControl.getParent().requestFocus());
        }
    }

    void selectAllRadioButtons() {
        for (Node node : inputNodeConfiguration.getChildren()) {
            ((RadioButton) node).setSelected(true);
        }
    }

    private boolean configureTextField(TextField field, int min, int max, String newValue,
                                       String oldValue) {
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

    private boolean configureDoubleTextField(TextField field, int min, int max, String newValue,
                                             String oldValue) {
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

    private Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(
                "  " + message + "  ");    // space for graphic reasons
        label.setOnMouseReleased(e -> popup.hide());
        popup.getContent().add(label);
        return popup;
    }

    private void showPopupMessage(final String message, Node node) {
        final Popup popup = createPopup(message);
        popup.setOnShown(e -> {
            popup.setX(node.localToScreen(node.getBoundsInLocal()).getMinX());
            popup.setY(node.localToScreen(node.getBoundsInLocal()).getMinY() - 30);
        });
        if (applicationController.getStage() != null) {
            popup.show(applicationController.getStage());
        }
    }

    void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
