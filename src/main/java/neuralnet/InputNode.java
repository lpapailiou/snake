package neuralnet;

import ai.PathGenerator;
import util.Direction;
import util.Setting;

import java.util.List;

public enum InputNode {

    WALL_LEFT("left ok"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return snake.get(0)[0] > 0 ? 1 : -1;
        }
    },
    WALL_RIGHT("right ok"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return Math.abs(Setting.getSettings().getBoardWidth()-1 - snake.get(0)[0]) > 0 ? 1 : -1;
        }
    },
    WALL_UP("up ok"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return snake.get(0)[1] > 0 ? 1 : -1;
        }
    },
    WALL_DOWN("down ok"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return Math.abs(Setting.getSettings().getBoardHeight()-1 - snake.get(0)[1]) > 0 ? 1 : -1;
        }
    },
    BODY_LEFT("body not blocking left"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.LEFT))) ? -1 : 1;
        }
    },
    BODY_RIGHT("body not blocking right"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.RIGHT))) ? -1 : 1;
        }
    },
    BODY_UP("body not blocking up"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.UP))) ? -1 : 1;
        }
    },
    BODY_DOWN("body not blocking down"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.DOWN))) ? -1 : 1;
        }
    },
    DISTANCE_GOODIE("distance to goodie"){
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return Math.abs(snake.get(0)[0] - goodie[0]) + Math.abs(snake.get(0)[1] - goodie[1]);
        }
    };

    private String tooltip;

    InputNode(String tooltip) {
        this.tooltip = tooltip;
    }

    public int getInputValue(List<int[]> snake, int[] goodie) {
        return 0;
    }
    public String getTooltip() { return tooltip; }

}
