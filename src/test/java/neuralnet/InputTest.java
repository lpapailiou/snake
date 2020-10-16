package neuralnet;

import game.Board;
import game.Snake;
import ai.netadapter.InputNode;
import org.junit.Test;

import java.util.Arrays;

public class InputTest {

    @Test
    public void inputTest() {
        Board board = new Board();
        Snake snake = board.getRealSnake();
        int[] goodie = board.getGoodie();

        System.out.println("snake: " + Arrays.toString(snake.getBody().get(0)));
        System.out.println("goodie: " + Arrays.toString(goodie));
        System.out.println();

        for (int i = 0; i < InputNode.values().length; i++) {
            System.out.println("node " + i + ": " + InputNode.values()[i].getInputValue(snake, goodie) + "     (" + InputNode.values()[i].getTooltip() + ")");
        }


    }


}
