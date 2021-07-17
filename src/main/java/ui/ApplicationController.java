package ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Config;
import main.Direction;
import main.Mode;
import main.State;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    @FXML
    private HBox rootElement;

    @FXML
    private GameController gameController;        // IDE says this is not used, but it is

    @FXML
    private ConfigController configController;    // IDE says this is not used, but it is

    private Stage stage;

    private State state = new State();
    private Config configuration = Config.getInstance();
    private Scene scene;
    private boolean isRealtimeStatisticsVerbose = true;
    private int statisticsPosition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        state.addGameListener(e -> {
            gameController.display(state.getGame());
            if (configuration.getMode() == Mode.NEURAL_NETWORK) {
                //configController.displayDirection(state.getGame().getDirection());
                if (isRealtimeStatisticsVerbose) {
                    //gameController.displayStats(state.getGenerationEntity(), state.getGame().getSnakeLength(),
                            //statisticsPosition);
                }
            }
        });
        state.addTimelineListener(e -> stopGame());
        state.addStartGameListener(e -> {
            if (configuration.getMode() == Mode.NEURAL_NETWORK) {
                configController.listenToNeuralNetwork(state.getNeuralNetwork());
            }
        });

        rootElement.sceneProperty().addListener(n -> {
            if (n != null) {
                this.scene = rootElement.getScene();
                listenToKeyboardEvents(rootElement.getScene());
            }
        });
        configController.setApplicationController(this);
        configController.setGameController(gameController);

        }


    void launchGame() {
        configController.setDisable(true);
        if (configuration.getMode() == Mode.NEURAL_NETWORK_DEMO) {
            configController.selectAllRadioButtons();
        }
        configuration.getMode().getAgent().setState(state).build();
    }

    void stopGame() {
        state.stopTimeline();
        configController.setDisable(false);
    }

    private void listenToKeyboardEvents(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            switch (key.getCode()) {
                case W:
                case UP:
                    state.setDirection(Direction.UP);
                    break;

                case A:
                case LEFT:
                    state.setDirection(Direction.LEFT);
                    break;

                case S:
                case DOWN:
                    state.setDirection(Direction.DOWN);
                    break;

                case D:
                case RIGHT:
                    state.setDirection(Direction.RIGHT);
                    break;

                case SPACE:
                case ENTER:
                    toggleGame();
                    break;

                case V:
                    isRealtimeStatisticsVerbose = !isRealtimeStatisticsVerbose;
                    break;

                case P:
                    statisticsPosition = statisticsPosition > 4 ? 0 : ++statisticsPosition;
                    break;
            }
        });
    }

    private void toggleGame() {     // when a text field is focused, a game launch will not be triggered
        if (!(scene.getFocusOwner() instanceof HBox) && !(scene.getFocusOwner() instanceof Button)) {
            Platform.runLater(() -> rootElement.requestFocus());
            return;
        }
        if (state.isTimelineRunning()) {
            stopGame();
        } else {
            launchGame();
        }
    }

    Scene getScene() {
        return scene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    Stage getStage() {
        return stage;
    }

}
