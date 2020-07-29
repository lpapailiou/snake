package ai;

import util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ai.PathUtils.exists;
import static ai.PathUtils.intersect;
import static util.Setting.*;

public class HamiltonPath {

    private static final int PHASE_THRESHOLD = 10;
    private static final int SEARCH_SCOPE = 6;
    private static final int PATIENCE = 3;
    private static int boardCenterX = BOARD_WIDTH/2;
    private static int boardCenterY = BOARD_HEIGHT/2;
    private static int boardX1 = 0;
    private static int boardY1 = 0;
    private static int boardX2 = BOARD_WIDTH;
    private static int boardY2 = BOARD_HEIGHT;

    private static List<Direction> tempPath = null;     // TODO: remove tempPath when testing is done

    static {
        if (boardCenterX%2 == 0) {
            --boardCenterX;
        }
        if (boardCenterY%2 == 0) {
            --boardCenterY;
        }
    }

    private static void resetBoardDimensions() {
        setBoardDimensions(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
    }

    private static void setBoardDimensions(int x1, int y1, int x2, int y2) {
        boardX1 = x1;
        boardY1 = y1;
        boardX2 = x2;
        boardY2 = y2;
    }

    private static void incrementBoardDimensions() {
        if (boardX1 <= 2 && boardY1 <= 2) {
            resetBoardDimensions();
        } else {
            if (boardX1 <= 2) {
                boardX1 = 0;
                boardX2 = BOARD_WIDTH;
            } else {
                boardX1 -= 2;
                boardX2 += 2;
            }
            if (boardY1 <= 2) {
                boardY1 = 0;
                boardY2 = BOARD_HEIGHT;
            } else {
                boardY1 -= 2;
                boardY2 += 2;
            }
        }
    }

    private static void decrementBoardDimensions() {
        boardX1 += 2;
        boardY1 += 2;
        boardX2 -= 2;
        boardY2 -= 2;
    }

    public static List<Direction> getStartCircle() {
        List<Direction> circle = new ArrayList();
        circle.add(Direction.UP);
        circle.add(Direction.RIGHT);
        circle.add(Direction.DOWN);
        circle.add(Direction.LEFT);
        return circle;
    }

    public static List<Direction> getShortcut(List<Direction> currentPath, int[] start, int[] goodie) {     // TODO: stop with shortcuts when snake is too large
        List<int[]> path = new ArrayList<>(PathUtils.getPathFromDirections(start, currentPath));
        path.add(0, start);

        int breakIndex = 0;
        for (int i = 0; i < path.size(); i++) {
            if (i > 1 && (intersect(path.get(i), start))) {     // TODO: target goodie?
                breakIndex = i;
                break;
                /*
                if (intersect(path.get(i), goodie) && i < (path.size()-1) && !Arrays.equals(goodie, path.get(i+1))) {
                    break;
                }*/
            } else if (Arrays.equals(path.get(i), goodie)) {     // too late, abort
                break;
            }
        }

        do {
            path.remove(0);
            breakIndex--;
        } while (breakIndex > 0);

        return PathUtils.getDirectionsFromPath(start, path);
    }

    public static List<Direction> getHamilton(int[] start) {    // TODO: backtracking may not be optimal yet
        if (tempPath != null) {
            return tempPath;
        }
        resetBoardDimensions();
        List<Direction> init = getStartCircle();
        List<int[]> path = PathUtils.getPathFromDirections(start, init);
        path.add(0, start);

        int phases = (int) Math.sqrt(BOARD_WIDTH*BOARD_HEIGHT) / PHASE_THRESHOLD;
        phases = (phases == 0) ? 1 : phases;
        int threshold ;
        int counter;
        int checkCounter = 0;
        int patienceThreshold = PATIENCE;
        List<List<int[]>> pathHistory = new ArrayList();
        pathHistory.add(0, new ArrayList<>(path));
        setBoardDimensions(boardCenterX-((BOARD_WIDTH / phases)/2), boardCenterY-((BOARD_HEIGHT / phases)/2), boardCenterX+((BOARD_WIDTH / phases)/2), boardCenterY+((BOARD_HEIGHT / phases)/2));
        while (path.size() < (BOARD_WIDTH*BOARD_HEIGHT)) {
            checkCounter++;
            //System.out.println("-----------------------------------------round " + checkCounter);
            incrementBoardDimensions();
            threshold = (boardX2-boardX1)*(boardY2-boardY1);
            counter = threshold*SEARCH_SCOPE;
            while (path.size() < threshold) {
                ////System.out.println("inner loop " + counter + " (threshold: " + threshold + ", round: " + checkCounter + ")");
                counter--;
                stretchCircle(path);
                if (counter == 0 && path.size() < threshold) {
                    if (patienceThreshold == 0) { // backtrack
                        --checkCounter;
                        //System.out.println("======================================================= BACKTRACK AT: " + pathHistory.size() + ", "+pathHistory.get(0).size());
                        if (pathHistory.size() > 1) {
                            pathHistory.remove(0);
                        }
                        path = new ArrayList<>(pathHistory.get(0));
                        patienceThreshold = PATIENCE;
                        decrementBoardDimensions();
                        threshold = (boardX2-boardX1)*(boardY2-boardY1);
                        counter = threshold * SEARCH_SCOPE;

                    } else {
                        path = new ArrayList<>(pathHistory.get(0)); // reset
                        patienceThreshold--;
                        counter = threshold * SEARCH_SCOPE;
                        //System.out.println("reset");
                    }
                }
                if (path.size() >= threshold) {
                    pathHistory.add(0, new ArrayList<>(path));
                    patienceThreshold = PATIENCE;
                }
            }
        }
        path.remove(0);
        //print(path);
        //tempPath = PathUtils.getDirectionsFromPath(new int[]{2, 2}, Arrays.asList(new int[][] {{2, 1}, {3, 1}, {3, 0}, {2, 0}, {1, 0}, {0, 0}, {0, 1}, {1, 1}, {1, 2}, {0, 2}, {0, 3}, {1, 3}, {2, 3}, {3, 3}, {3, 2}, {2, 2}}));
        tempPath = PathUtils.getDirectionsFromPath(start, path);
        //System.out.println(Arrays.deepToString(path.toArray()).replace("[", "{").replace("]", "}"));
        return PathUtils.getDirectionsFromPath(start, path);
        //return tempPath;
    }

    private static boolean createPath(List<int[]> path, int threshold) {
        int counter = threshold*6;
        while (path.size() < threshold) {
            counter--;
            stretchCircle(path);
            if (counter == 0 && path.size() < threshold) {
                return false;
            }
        }
        return true;
    }

    private static void print(List<int[]> list) {
        for (int[] i : list) {
            System.out.print(Arrays.toString(i) + ", ");
        }
        System.out.println();
    }

    public static void stretchCircle(List<int[]> path) {
        int index = RANDOM.nextInt(path.size()-1);  // prevents taking the last element
        int[] coord1 = path.get(index);
        int[] coord2 = path.get(index+1);
        boolean horizontal = false;
        if (coord1[1] == coord2[1]) {
            horizontal = true;
        }
        int[] nextcoord1 = null;
        int[] nextcoord2 = null;
        Direction dir = Direction.NONE;
        Direction[] axis = Direction.getAxisDirections(!horizontal);
        for (Direction d : axis) {
            nextcoord1 = Direction.getNextCoord(coord1,d);
            if (isOnBoard(nextcoord1) && !exists(path, nextcoord1)) {
                nextcoord2 = Direction.getNextCoord(coord2, d);
                dir = d;
                break;
            }
        }

        if (nextcoord2 == null) {
            return;
        }

        if (isOnBoard(nextcoord2) && !exists(path, nextcoord2)) {
            path.add(index + 1, nextcoord1);
            path.add(index + 2, nextcoord2);

            boolean success = stretchFurther(path, ++index, nextcoord1, nextcoord2, dir);
            while (success) {
                success = stretchFurther(path, ++index, nextcoord1, nextcoord2, dir);
            }
        }
    }

    public static boolean stretchFurther(List<int[]> path, int index1, int[] coord1, int[] coord2, Direction dir) {
        int[] nextcoord1 = Direction.getNextCoord(coord1, dir);
        int[] nextcoord2 = Direction.getNextCoord(coord2, dir);
        if (isOnBoard(nextcoord1) && !exists(path, nextcoord1) && isOnBoard(nextcoord2) && !exists(path, nextcoord2)) {
            path.add(index1 + 1, nextcoord1);
            path.add(index1 + 2, nextcoord2);
            return true;
        }
        return false;
    }

    public static boolean isOnBoard(int[] next) {
        if (next[0] < boardX1 || next[0] >= boardX2) {
            return false;
        } else return next[1] >= boardY1 && next[1] < boardY2;
    }
}
