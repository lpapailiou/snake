package util;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyParser {

    private KeyParser() {}

    public static Direction handleKeyPress(KeyEvent e) {
        KeyCode key = e.getCode();
        Direction dir = Direction.NONE;
        switch (key) {
            case UP:
                dir = Direction.UP;
                break;
            case DOWN:
                dir = Direction.DOWN;
                break;
            case LEFT:
                dir = Direction.LEFT;
                break;
            case RIGHT:
                dir = Direction.RIGHT;
                break;
            case ENTER:
            case SPACE:
            case Y:
                dir = Direction.NONE;
                break;
            case Q:
            case ESCAPE:
            case N:
                dir = Direction.GONE;
        }
        return dir;
    }
}
