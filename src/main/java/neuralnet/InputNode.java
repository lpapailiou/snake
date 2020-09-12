package neuralnet;

import ai.PathGenerator;
import util.Direction;

import java.util.List;

import static util.Setting.BOARD_HEIGHT;
import static util.Setting.BOARD_WIDTH;

public enum InputNode {

    WALL_LEFT{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return snake.get(0)[0] > 0 ? 1 : -1;
        }
    },
    WALL_RIGHT{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return Math.abs(BOARD_WIDTH - snake.get(0)[0]) > 0 ? 1 : -1;
        }
    },
    WALL_UP{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return snake.get(0)[1] > 0 ? 1 : -1;
        }
    },
    WALL_DOWN{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return Math.abs(BOARD_HEIGHT - snake.get(0)[1]) > 0 ? 1 : -1;
        }
    },
    BODY_LEFT{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.LEFT))) ? -1 : 1;
        }
    },
    BODY_RIGHT{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.RIGHT))) ? -1 : 1;
        }
    },
    BODY_UP{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.UP))) ? -1 : 1;
        }
    },
    BODY_DOWN{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return (PathGenerator.exists(snake, Direction.getNextCoord(snake.get(0), Direction.DOWN))) ? -1 : 1;
        }
    },
    DISTANCE_GOODIE{
        @Override
        public int getInputValue(List<int[]> snake, int[] goodie) {
            return Math.abs(snake.get(0)[0] - goodie[0]) + Math.abs(snake.get(0)[1] - goodie[1]);
        }
    };

    public int getInputValue(List<int[]> snake, int[] goodie) {
        return 0;
    }

}
