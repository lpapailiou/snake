package ai;

import application.GamePanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HamiltonBot extends Bot {
    List<Direction> staticPath = getPath();
    List<Direction> _path = new ArrayList(staticPath);
    List<int[]> snake;
    int counter = 2;


    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            if (running) {
                if (!_path.isEmpty()) {
                    //_path = HamiltonPath.getShortcut(_path, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie());
                    running = GamePanel.move(_path.get(_path.size() - 1));
                    _path.remove(_path.size() - 1);
                    counter--;
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



    private List<Direction> getPath() {
        snake = GamePanel.getPanel().getSnake();
        return new ArrayList<>(HamiltonPath.getHamilton(snake.get(0)));
    }

}
