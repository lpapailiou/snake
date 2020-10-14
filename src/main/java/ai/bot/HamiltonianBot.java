package ai.bot;

import ai.HamiltonianPathGenerator;
import application.GamePanel;
import java.util.ArrayList;
import java.util.List;

public class HamiltonianBot extends Bot {
    List<int[]> staticPath = getPath();
    List<int[]> _path = getNextSection();
    List<int[]> snake;
    int index = 0;

    @Override
    protected void run() {
        if (!_path.isEmpty()) {
            boolean isRunning = GamePanel.move(_path.get(0), _path, staticPath);
            if (!isRunning) {
                stop();
            }
            _path.remove(0);
        } else {
            stop();
        }
        if (_path.isEmpty()) {
            _path = getNextSection();
        }
    }

    @Override
    protected List<int[]> getPath() {
        snake = GamePanel.getPanel().getSnake();
        return new ArrayList<>(HamiltonianPathGenerator.getHamilton(snake.get(0)));
    }

    private List<int[]> getNextSection() {
        List<int[]> section = HamiltonianPathGenerator.getPathBetween(staticPath, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie());
        section.remove(0);
        return section;
    }

}
