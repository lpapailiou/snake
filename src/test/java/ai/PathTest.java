package ai;

import org.junit.Test;
import util.Direction;

import java.util.Arrays;
import java.util.List;

public class PathTest {

    @Test
    public void pathConversionTest2() {
        List<Direction> directions = PathUtils.getDirectionsFromPath(new int[]{2, 2}, Arrays.asList(new int[][] {{2, 1}, {3, 1}, {3, 0}, {2, 0}, {1, 0}, {0, 0}, {0, 1}, {1, 1}, {1, 2}, {0, 2}, {0, 3}, {1, 3}, {2, 3}, {3, 3}, {3, 2}, {2, 2}}));
        for (Direction dir : directions) {
            System.out.print(dir.name() +", ");
        }

        List<int[]> path = PathUtils.getPathFromDirections(new int[]{2, 2}, directions);
        System.out.println();
        for (int[] coord : path) {
            System.out.print("("+coord[0]+", "+coord[1]+"), ");
        }
        System.out.println();
        path = Arrays.asList(new int[][] {{2, 1}, {3, 1}, {3, 0}, {2, 0}, {1, 0}, {0, 0}, {0, 1}, {1, 1}, {1, 2}, {0, 2}, {0, 3}, {1, 3}, {2, 3}, {3, 3}, {3, 2}, {2, 2}});
        for (int[] coord : path) {
            System.out.print("("+coord[0]+", "+coord[1]+"), ");
        }
        /*
        LEFT, UP, RIGHT, RIGHT, RIGHT, DOWN, LEFT, DOWN, RIGHT, DOWN, LEFT, LEFT, LEFT, UP, RIGHT, UP,
        (2, 1), (3, 1), (3, 0), (2, 0), (1, 0), (0, 0), (0, 1), (1, 1), (1, 2), (0, 2), (0, 3), (1, 3), (2, 3), (3, 3), (3, 2), (2, 2),
         */
    }



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
