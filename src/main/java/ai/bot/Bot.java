package ai.bot;

import util.State;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import util.Setting;

import java.util.List;

public abstract class Bot {
    private boolean running = true;
    private long id = System.currentTimeMillis();
    private Timeline timeline;

    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(Setting.getSettings().getBotSpeed()), event -> {
            if (running) {
                run();
                if (State.getInstance().isInterrupted()) {
                    running = false;
                    State.getInstance().setInterrupt(false);
                    stop();
                }
            } else {
                if (timeline != null) {
                    timeline.stop();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    protected boolean isRunning() { return running; }
    protected void stop() {
        running = false;
    }
    protected abstract void run();
    protected abstract List<int[]> getPath();
}
