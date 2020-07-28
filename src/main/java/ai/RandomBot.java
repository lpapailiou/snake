package ai;

import application.GamePanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import util.Direction;

import java.util.List;

import static ai.PathGenerator.*;
import static util.Setting.BOT_SPEED;

public class RandomBot extends Bot {

    List<Direction> path = getMyPath();

    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            if (running) {
                if (!path.isEmpty()) {
                    running = GamePanel.move(path.get(path.size() - 1));
                    path.remove(path.size() - 1);
                } else {
                    running = false;
                }
                if (path.isEmpty()) {
                    path = getMyPath();
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

    private List<Direction> getMyPath() {
        return getExploringPath(GamePanel.getPanel().getSnake(), GamePanel.getPanel().getGoodie());
        //return getHamiltonPath(GamePanel.getPanel().getSnake());
    }
}
