package netadapter;

import ai.PathGenerator;
import game.Snake;
import util.Direction;
import util.Setting;

import static ai.PathGenerator.exists;

public enum InputNode {

    // ============================ DISTANCE TO WALL ============================
    WALL_UP("distance to upper wall"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return 1/(snake.getBody().get(0)[1]+0.001);
        }
    },

    WALL_RIGHT("distance to right wall"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return 1/(Math.abs(Setting.getSettings().getBoardWidth()-1 - snake.getBody().get(0)[0])+0.001);
        }
    },
    WALL_DOWN("distance to lower wall"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return 1/(Math.abs(Setting.getSettings().getBoardHeight()-1 - snake.getBody().get(0)[1])+0.001);
        }
    },
    WALL_LEFT("distance to left wall"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return 1/(snake.getBody().get(0)[0]+0.001);
        }
    },

    /*
    WALL_UP_RIGHT("distance to wall diagonally up/right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int x = Setting.getSettings().getBoardWidth()-1-snakeHead[0];
            int y = snakeHead[1];
            return 1/(x + y + 0.001);
        }
    },
    WALL_DOWN_RIGHT("distance to wall diagonally down/right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int x = Setting.getSettings().getBoardWidth()-1-snakeHead[0];
            int y = Setting.getSettings().getBoardHeight()-1-snakeHead[1];
            return 1/(x + y + 0.001);
        }
    },
    WALL_DOWN_LEFT("distance to wall diagonally down/left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int x = snakeHead[0];
            int y = Setting.getSettings().getBoardHeight()-1-snakeHead[1];
            return 1/(x + y + 0.001);
        }
    },
    WALL_UP_LEFT("distance to wall diagonally up/left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int x = snakeHead[0];
            int y = snakeHead[1];
            return 1/(x + y + 0.001);
        }
    },*/

    GOODIE_UP("vision of goodie up"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int distanceY = snakeHead[1] - goodie[1];
            if (snakeHead[0]==goodie[0] && distanceY > 0) {
                return 1000;
            }
            return 0;
        }
    },
    GOODIE_RIGHT("vision of goodie right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int distanceY = snakeHead[0] - goodie[0];
            if (snakeHead[1]==goodie[1] && distanceY < 0) {
                return 1000;
            }
            return 0;
        }
    },
    GOODIE_DOWN("vision of goodie down"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int distanceY = snakeHead[1] - goodie[1];
            if (snakeHead[0]==goodie[0] && distanceY < 0) {
                return 1000;
            }
            return 0;
        }
    },
    GOODIE_LEFT("vision of goodie left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            int distanceY = snakeHead[0] - goodie[0];
            if (snakeHead[1]==goodie[1] && distanceY > 0) {
                return 1000;
            }
            return 0;
        }
    },

    BODY_UP("distance to body up"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int index = 0;
            for (int i = pos[1]-1; i >= 0; i--) {
                pos[1] = i;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
                index++;
            }
            return 1/(index+0.001);
        }
    },
    BODY_RIGHT("distance to body right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int end = Setting.getSettings().getBoardWidth();
            int index = 0;
            for (int i = pos[0]+1; i < end; i++) {
                pos[0] = i;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
                index++;
            }
            return 1/(index+0.001);
        }
    },

    BODY_DOWN("distance to body down"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int end = Setting.getSettings().getBoardHeight();
            int index = 0;
            for (int i = pos[1]+1; i < end; i++) {
                pos[1] = i;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
                index++;
            }
            return 1/(index+0.001);
        }
    },

    BODY_LEFT("distance to body left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int index = 0;
            for (int i = pos[0]-1; i >= 0; i--) {
                pos[0] = i;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
                index++;
            }
            return 1/(index+0.001);
        }
    },







    // ============================ CUSTOM NODES ============================
    DISTANCE_GOODIE("distance to goodie"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return Math.abs(snake.getBody().get(0)[0] - goodie[0]) + Math.abs(snake.getBody().get(0)[1] - goodie[1]);
        }
    },

    /*
    TIME_REMAINING("time remaining"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return snake.getTimeout();
        }
    },*/













    // ============================ DISTANCE TO GOODIE ============================



    GOODIE_UP_RIGHT("vision of goodie diagonally up/right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            if (snakeHead[0]+goodie[0] == snakeHead[1]+goodie[1]) {
                if (!(snakeHead[0]-goodie[0] > 0) && (snakeHead[1]-goodie[1] > 0)) {
                    return  1;
                }
            }
            return 0;
        }
    },

    GOODIE_DOWN_RIGHT("vision of goodie diagonally down/right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            if (snakeHead[0]+goodie[0] == snakeHead[1]+goodie[1]) {
                if (!(snakeHead[0]-goodie[0] > 0) && !(snakeHead[1]-goodie[1] > 0)) {
                    return  1;
                }
            }
            return 0;
        }
    },

    GOODIE_DOWN_LEFT("vision of goodie diagonally down/left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            if (snakeHead[0]+goodie[0] == snakeHead[1]+goodie[1]) {
                if ((snakeHead[0]-goodie[0] > 0) && !(snakeHead[1]-goodie[1] > 0)) {
                    return  1;
                }
            }
            return 0;
        }
    },

    GOODIE_UP_LEFT("vision of goodie diagonally up/left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] snakeHead = snake.getBody().get(0);
            if (snakeHead[0]+goodie[0] == snakeHead[1]+goodie[1]) {
                if ((snakeHead[0]-goodie[0] > 0) && (snakeHead[1]-goodie[1] > 0)) {
                    return  1;
                }
            }
            return 0;
        }
    },


    // ============================ DISTANCE TO BODY NEW ============================



    BODY_UP_RIGHT("distance to body up/right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int targetW = Setting.getSettings().getBoardWidth() - pos[0];
            int targetH = pos[1];
            int max = Math.max(targetH, targetW);
            for (int i = 1; i < max; i++) {
                pos[0] = pos[0]+1;
                pos[1] = pos[1]-1;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
            }
            return 1/(pos[0]+pos[1]+0.001);
        }
    },
    BODY_DOWN_RIGHT("distance to body down/right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int targetW = Setting.getSettings().getBoardWidth() - pos[0];
            int targetH = Setting.getSettings().getBoardHeight() - pos[1];
            int max = Math.max(targetH, targetW);
            for (int i = 1; i < max; i++) {
                pos[0] = pos[0]+1;
                pos[1] = pos[1]+1;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
            }
            return 1/(pos[0]+pos[1]+0.001);
        }
    },
    BODY_DOWN_LEFT("distance to body down/left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int targetW = pos[0];
            int targetH = Setting.getSettings().getBoardHeight() - pos[1];
            int max = Math.max(targetH, targetW);
            for (int i = 1; i < max; i++) {
                pos[0] = pos[0]-1;
                pos[1] = pos[1]+1;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
            }
            return 1/(pos[0]+pos[1]+0.001);
        }
    },
    BODY_UP_LEFT("distance to body up/left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            int[] pos = snake.getBody().get(0).clone();
            int targetW = pos[0];
            int targetH = pos[1];
            int max = Math.max(targetH, targetW);
            for (int i = 1; i < max; i++) {
                pos[0] = pos[0]-1;
                pos[1] = pos[1]-1;
                if (exists(snake.getBody(), pos)) {
                    break;
                }
            }
            return 1/(pos[0]+pos[1]+0.001);
        }
    }
    ;







    // ============================ DISTANCE TO BODY OLD ============================
    /*
    DISTANCE_BODY_LEFT("body not blocking left"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.LEFT))) ? -1 : 1;
        }
    },
    DISTANCE_BODY_RIGHT("body not blocking right"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.RIGHT))) ? -1 : 1;
        }
    },
    DISTANCE_BODY_UP("body not blocking up"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.UP))) ? -1 : 1;
        }
    },
    DISTANCE_BODY_DOWN("body not blocking down"){
        @Override
        public double getInputValue(Snake snake, int[] goodie) {
            return (PathGenerator.exists(snake.getBody(), Direction.getNextCoord(snake.getBody().get(0), Direction.DOWN))) ? -1 : 1;
        }
    };*/


    private String tooltip;

    InputNode(String tooltip) {
        this.tooltip = tooltip;
    }

    public double getInputValue(Snake snake, int[] goodie) {
        return 0;
    }
    public String getTooltip() { return tooltip; }

}
