package ai.bot;

import java.util.List;

public class HamiltonianShortcutBot extends Bot {
    @Override
    protected void run() {

    }

    @Override
    protected List<int[]> getPath() {
        return null;
    }
    /*
    List<int[]> staticPath = getPath();
    List<int[]> _path = HamiltonianPathGenerator.getPathSection(staticPath, GamePanel.getPanel().getSnake(), GamePanel.getPanel().getGoodie());
    List<int[]> snake;

    @Override
    protected void run() {
        if (!_path.isEmpty()) {
            boolean isRunning = GamePanel.move(_path.get(0), staticPath, _path);
            _path.remove(0);
            if (!isRunning) {
                stop();
            }
        } else {
            stop();
        }
        if (_path.isEmpty()) {
            _path = HamiltonianPathGenerator.getPathSection(staticPath, snake, GamePanel.getPanel().getGoodie());
        }
    }

    @Override
    protected List<int[]> getPath() {
        snake = GamePanel.getPanel().getSnake();
        return new ArrayList<>(HamiltonianPathGenerator.getHamilton(snake.get(0)));
    }


     */

}
