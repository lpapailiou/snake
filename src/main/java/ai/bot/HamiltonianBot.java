package ai.bot;

import ai.HamiltonianPathGenerator;
import ai.bot.Bot;
import application.GamePanel;
import util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HamiltonianBot extends Bot {
    List<int[]> staticPath = getHamPath();
    List<int[]> _path = getNextSection();
    List<int[]> snake;
    int index = 0;

    @Override
    protected void run() {
        if (!_path.isEmpty()) {
            running = GamePanel.move(_path.get(0));
            _path.remove(0);
        } else {
            running = false;
        }
        if (_path.isEmpty()) {
            _path = getNextSection();
        }
    }

    @Override
    protected List<Direction> getPath() {
        return null;
    }

    protected List<int[]> getHamPath() {
        snake = GamePanel.getPanel().getSnake();
        return new ArrayList<>(HamiltonianPathGenerator.getHamilton(snake.get(0)));
    }

    private List<int[]> getNextSection() {
        List<int[]> section = HamiltonianPathGenerator.getPathBetween(staticPath, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie());
        section.remove(0);
        return section;
    }

}
