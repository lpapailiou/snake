package ui.util;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import main.Mode;

public class ControlsAnimator {

    private final ComboBox<String> modeSelector;
    private final HBox baseControls;
    private final HBox actionControls;
    private final HBox neuralNetworkControls;
    private final HBox statisticsControls;

    public ControlsAnimator(
            ComboBox<String> modeSelector, HBox baseControls,
            HBox actionControls, HBox neuralNetworkControls,
            HBox statisticControls) {
        this.modeSelector = modeSelector;
        this.baseControls = baseControls;
        this.actionControls = actionControls;
        this.neuralNetworkControls = neuralNetworkControls;
        this.statisticsControls = statisticControls;
    }

    public void animateModeTransition(Mode newMode, Mode oldMode, Scene scene) {
        if (scene == null) {
            return;
        }

        if (oldMode == newMode) {
            return;
        }

        if (oldMode != Mode.NEURAL_NETWORK && newMode != Mode.NEURAL_NETWORK) {
            return;
        }

        modeSelector.hide();
        final Duration transitionDuration = Duration.seconds(0.3);
        TranslateTransition baseControlsTransition = new TranslateTransition(transitionDuration,
                baseControls);

        if (newMode == Mode.NEURAL_NETWORK) {
            baseControlsTransition.setFromY(237);
            baseControlsTransition.setToY(0);
        } else {
            baseControlsTransition.setFromY(-237);
            baseControlsTransition.setToY(0);
        }

        TranslateTransition actionControlsTransition = new TranslateTransition(transitionDuration,
                actionControls);
        if (newMode == Mode.NEURAL_NETWORK) {
            actionControlsTransition.setFromX(-177);
            actionControlsTransition.setToX(0);
            actionControlsTransition.setFromY(-235);
            actionControlsTransition.setToY(0);
        } else {
            actionControlsTransition.setFromX(177);
            actionControlsTransition.setToX(0);
            actionControlsTransition.setFromY(235);
            actionControlsTransition.setToY(0);
        }

        ScaleTransition actionControlsScaleTransition = new ScaleTransition(transitionDuration,
                actionControls);
        if (newMode == Mode.NEURAL_NETWORK) {
            actionControlsScaleTransition.setFromX(2);
            actionControlsScaleTransition.setToX(1);
        } else {
            actionControlsScaleTransition.setFromX(0.5);
            actionControlsScaleTransition.setToX(1);
        }

        neuralNetworkControls.setOpacity(0);
        FadeTransition neuralNetworkTransition = new FadeTransition((Duration.seconds(0.0001)),
                neuralNetworkControls);
        if (newMode == Mode.NEURAL_NETWORK) {
            neuralNetworkTransition.setFromValue(0);
            neuralNetworkTransition.setToValue(1);
        }

        statisticsControls.setOpacity(0);
        FadeTransition statisticTransition = new FadeTransition((Duration.seconds(0.0001)),
                statisticsControls);
        if (newMode == Mode.NEURAL_NETWORK) {
            statisticTransition.setFromValue(0);
            statisticTransition.setToValue(1);
        }

        ParallelTransition para1 = new ParallelTransition();
        para1.getChildren()
                .addAll(baseControlsTransition, actionControlsTransition, actionControlsScaleTransition);
        ParallelTransition para2 = new ParallelTransition();
        para2.getChildren().addAll(neuralNetworkTransition, statisticTransition);
        SequentialTransition seq = new SequentialTransition();
        seq.getChildren().addAll(para1, para2);
        seq.play();
    }

}
