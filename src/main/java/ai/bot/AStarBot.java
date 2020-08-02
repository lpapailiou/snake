package ai.bot;

import ai.AStarPathGenerator;
import ai.HamiltonianPathGenerator;
import application.GamePanel;
import util.Direction;

import java.util.ArrayList;
import java.util.List;

public class AStarBot extends Bot {


    List<int[]> staticPath = getHamPath();
    List<int[]> _goodiepath = getGoodiePath();
    List<int[]> _path = getMyPath();

    @Override
    protected void run() {
        if (!_path.isEmpty()) {
            running = GamePanel.move(_path.get(0), _goodiepath , _path);
            _path.remove(0);
        } else {
            running = false;
        }
        if (_path.isEmpty()) {
            _path = getMyPath();

        }
    }

    @Override
    protected List<Direction> getPath() {
        return null;
    }

    private List<int[]> getMyPath() {
        _goodiepath = getGoodiePath();
        _path = AStarPathGenerator.getAStarPath(_goodiepath, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie());
        _path.remove(0);
        return _path;
    }

    protected List<int[]> getHamPath() {
        return new ArrayList<>(HamiltonianPathGenerator.getHamilton(GamePanel.getPanel().getSnake().get(0)));
    }

    protected List<int[]> getGoodiePath() {
        List<int[]> p = HamiltonianPathGenerator.getPathBetween(staticPath, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie());
        return p;
    }
}
