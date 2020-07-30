package ai.bot;

import application.GamePanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import util.Direction;

import java.util.ArrayList;
import java.util.List;

public abstract class Bot {
    boolean running = true;
    Timeline timeline;

    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            if (running) {
                run();
            } else {
                if (timeline != null) {
                    timeline.stop();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    protected abstract void run();
    protected abstract List<Direction> getPath();
}
