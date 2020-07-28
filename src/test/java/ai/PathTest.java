package ai;

import org.junit.Test;
import util.Direction;

import java.util.Arrays;
import java.util.List;

public class PathTest {


    @Test
    public void testPathConversion() {
        int[] start = new int[] {0,0};
        Direction[] directions = new Direction[] {Direction.LEFT, Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN};
        List<int[]> path = PathUtils.getPathFromDirections(start, Arrays.asList(directions));

        for (int[] coord : path) {
            System.out.print("("+coord[0]+", "+coord[1]+"), ");
        }
        System.out.println();

        List<Direction> directionsConvertedBack = PathUtils.getDirectionsFromPath(start, path);

        for (Direction dir : directionsConvertedBack) {
            System.out.print(dir.name() +", ");
        }
    }
}
