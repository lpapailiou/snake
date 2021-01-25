package application;

import util.State;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import util.Mode;
import util.Setting;
import util.Theme;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ConfigPanel implements Initializable {

    private ObservableList<String> modeList = FXCollections.observableArrayList(Arrays.stream(Mode.values()).map(Mode::getLabel).collect(Collectors.toList()));
    private ObservableList<String> colorList = FXCollections.observableArrayList(Arrays.stream(Theme.values()).map(Enum::name).collect(Collectors.toList()));
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
    private ComboBox<String> modeChooser;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button statisticsButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        setUpMainControl();
        setUpModeControl();
        setUpButtons();
        Platform.runLater(() -> startButton.getParent().requestFocus());
    }

    public static ConfigPanel getPanel() {
        return instance;
    }

    void lockInput(boolean lock) {
        boardWithControl.setDisable(lock);
        boardHeightControl.setDisable(lock);
        colorSchemeChooser.setDisable(lock);
        modeChooser.setDisable(lock);
        startButton.setDisable(lock);
        stopButton.setDisable(!lock);
        //statisticsButton.setDisable(lock);
        Mode mode = Arrays.stream(Mode.values()).filter(e -> e.getLabel().equals(modeChooser.getValue())).findFirst().get();
        NeuralNetConfigPanel.getPanel().lockInput(lock, mode);
        Platform.runLater(() -> startButton.getParent().requestFocus());
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

        stopButton.setOnAction(e -> {
            State.getInstance().setInterrupt(true);
            GamePanel.stop();
        });
        stopButton.setDisable(true);
    }

    private void setUpModeControl() {
        modeChooser.setItems(modeList);
        modeChooser.getSelectionModel().select(1);
        modeChooser.setOnAction( e -> updateMode());
        updateMode();
    }

    private void updateMode() {
        Mode mode = Arrays.stream(Mode.values()).filter(e -> e.getLabel().equals(modeChooser.getValue())).findFirst().get();
        Setting.getSettings().setBot(mode.getBotTemplate());
        if (NeuralNetConfigPanel.getPanel() != null) {
            NeuralNetConfigPanel.getPanel().updateMode(mode);
        }
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

    private void setUpMainControl() {
        boardWithControl.setText(Setting.getSettings().getBoardWidth() + "");
        boardHeightControl.setText(Setting.getSettings().getBoardHeight() + "");
        AtomicReference<String> tempWidth = new AtomicReference<String>(Setting.getSettings().getBoardWidth() + "");
        boardWithControl.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempWidth.toString();
            tempWidth.set(boardWithControl.getText());
            if (configureTextField(boardWithControl, 1, 100, tempWidth.toString(), previousValue)) {
                Setting.getSettings().setBoardWidth(Integer.parseInt(tempWidth.toString()));
                GamePanel.getPanel().setDimensions();
            }
        });
        AtomicReference<String> tempHeight = new AtomicReference<String>(Setting.getSettings().getBoardHeight() + "");
        boardHeightControl.focusedProperty().addListener((o, oldValue, newValue) -> {
            String previousValue = tempHeight.toString();
            tempHeight.set(boardHeightControl.getText());
            if (configureTextField(boardHeightControl, 1, 100, tempHeight.toString(), previousValue)) {
                Setting.getSettings().setBoardHeight(Integer.parseInt(tempHeight.toString()));
                GamePanel.getPanel().setDimensions();
            }
        });

        colorSchemeChooser.setItems(colorList);
        colorSchemeChooser.getSelectionModel().select(0);
        colorSchemeChooser.setOnAction( e -> updateColorScheme());
    }

    private void updateColorScheme() {
        List<String> cssList = Arrays.stream(Theme.values()).map(Theme::getCss).collect(Collectors.toList());
        for (Object str : cssList) {
            String sheet = (String) str;
            configPanel.getScene().getStylesheets().removeIf(s -> s.matches(Objects.requireNonNull(Driver.class.getClassLoader().getResource(sheet)).toExternalForm()));
        }
        String selection = colorSchemeChooser.getValue();
        Theme scheme = Theme.valueOf(selection);
        configPanel.getScene().getStylesheets().remove(Setting.getSettings().getTheme().getCss());
        Setting.getSettings().setTheme(scheme);
        GamePanel.getPanel().paint();
        configPanel.getScene().setFill(Setting.getSettings().getTheme().getBackgroundColor());
        configPanel.getScene().getStylesheets().add(Objects.requireNonNull(Driver.class.getClassLoader().getResource(scheme.getCss())).toExternalForm());
        NeuralNetConfigPanel.getPanel().updateNetwork();
    }

}
