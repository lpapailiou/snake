package ai.bot;

import java.util.List;

public class AStarBot extends Bot {
    @Override
    protected void run() {

    }

    @Override
    protected List<int[]> getPath() {
        return null;
    }
/*
    List<int[]> staticPath = getPath();
    List<int[]> _goodiepath = getGoodiePath();
    List<int[]> _path = getMyPath();

    @Override
    protected void run() {
        if (!_path.isEmpty()) {
            boolean isRunning = GamePanel.move(_path.get(0), _goodiepath , _path);
            if (!isRunning) {
                stop();
            }
            _path.remove(0);
        } else {
            stop();
        }
        if (_path.isEmpty()) {
            _path = getMyPath();

        }
    }

    @Override
    protected List<int[]> getPath() {
        return new ArrayList<>(HamiltonianPathGenerator.getHamilton(GamePanel.getPanel().getSnake().get(0)));
    }

    private List<int[]> getMyPath() {
        _goodiepath = getGoodiePath();
        _path = AStarPathGenerator.getAStarPath(_goodiepath, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie(), true);
        _path.remove(0);
        return _path;
    }

    protected List<int[]> getGoodiePath() {
        List<int[]> p = HamiltonianPathGenerator.getPathBetween(staticPath, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie());
        return p;
    }*/
}
