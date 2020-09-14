package neuralnet;

import ai.PathGenerator;
import game.Snake;
import util.Direction;
import util.Setting;

import java.util.List;

public enum InputNode {

    WALL_LEFT("distance to left wall"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return snake.getBody().get(0)[0];
        }
    },
    WALL_RIGHT("distance to right wall"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return Math.abs(Setting.getSettings().getBoardWidth()-1 - snake.getBody().get(0)[0]);
        }
    },
    WALL_UP("distance to upper wall"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return snake.getBody().get(0)[1];
        }
    },
    WALL_DOWN("distance to lower wall"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return Math.abs(Setting.getSettings().getBoardHeight()-1 - snake.getBody().get(0)[1]);
        }
    },
    BODY_LEFT("body not blocking left"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.LEFT))) ? -1 : 1;
        }
    },
    BODY_RIGHT("body not blocking right"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.RIGHT))) ? -1 : 1;
        }
    },
    BODY_UP("body not blocking up"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.UP))) ? -1 : 1;
        }
    },
    BODY_DOWN("body not blocking down"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.DOWN))) ? -1 : 1;
        }
    },
    DISTANCE_GOODIE("distance to goodie"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return Math.abs(snake.getBody().get(0)[0] - goodie[0]) + Math.abs(snake.getBody().get(0)[1] - goodie[1]);
        }
    },
    TIME_REMAINING("time remaining"){
        @Override
        public int getInputValue(Snake snake, int[] goodie) {
            return snake.getTimeout();
        }
    };

    private String tooltip;

    InputNode(String tooltip) {
        this.tooltip = tooltip;
    }

    public int getInputValue(Snake snake, int[] goodie) {
        return 0;
    }
    public String getTooltip() { return tooltip; }

}
