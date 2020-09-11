package neuralnet;

import game.Board;
import org.junit.Test;
import util.Direction;

public class BoardHandlerTest {


    @Test
    public void boardTest() {
        Board b = new Board();
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);
        b.moveSnake(Direction.LEFT);

        System.out.println(b.getResult());
    }
}
