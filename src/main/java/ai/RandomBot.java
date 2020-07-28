package ai;

import application.GamePanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import util.Direction;

import java.util.ArrayList;
import java.util.List;

import static ai.HamiltonPath.getHamilton;
import static ai.PathGenerator.*;
import static util.Setting.BOT_SPEED;

public class RandomBot extends Bot {
    List<Direction> staticPath = getMyPath();
    List<Direction> _path = new ArrayList(staticPath);


    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
            if (running) {
                if (!_path.isEmpty()) {
                    running = GamePanel.move(_path.get(_path.size() - 1));
                    _path.remove(_path.size() - 1);
                } else {
                    running = false;
                }
                if (_path.isEmpty()) {
                    _path = new ArrayList(staticPath);
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
        //return getExploringPath(GamePanel.getPanel().getSnake(), GamePanel.getPanel().getGoodie());
        List<int[]> snake = GamePanel.getPanel().getSnake();
        int[] goodie = GamePanel.getPanel().getGoodie();
        //return getHamiltonPath(GamePanel.getPanel().getSnake());
        //return PathUtils.getShortestSafePath(snake, goodie);
        List<Direction> path = HamiltonPath.getHamilton(snake.get(0));
        int counter = 0;
        while (path == null) {
            System.out.println("retry: # "+ ++counter);
            path = HamiltonPath.getHamilton(snake.get(0));
        }
        return new ArrayList<>(path);
    }
}
