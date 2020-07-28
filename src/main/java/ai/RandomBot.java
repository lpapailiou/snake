package ai;

import application.GamePanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import util.Direction;
import static util.Setting.BOT_SPEED;

public class RandomBot extends Bot {

    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(BOT_SPEED), event -> {
            if (running) {
                running = GamePanel.move(Direction.getRandomDirection());
            } else {
                if (timeline != null) {
                    timeline.stop();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
