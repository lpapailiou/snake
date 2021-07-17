package neuralnet;

import game.Game;
import main.Direction;
import org.junit.Test;

public class GameHandlerTest {


    @Test
    public void boardTest() {
        Game b = new Game();
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
