package ai;

import util.Direction;

import java.util.*;

import static ai.PathGenerator.exists;
import static ai.PathGenerator.intersect;
import static util.Setting.*;

public class HamiltonianPathGenerator extends PathGenerator {

    private static final int PHASE_THRESHOLD = 10;
    private static final int SEARCH_SCOPE = 6;
    private static final int PATIENCE = 3;
    private static int boardCenterX = BOARD_WIDTH/2;
    private static int boardCenterY = BOARD_HEIGHT/2;
    private static int boardX1 = 0;
    private static int boardY1 = 0;
    private static int boardX2 = BOARD_WIDTH;
    private static int boardY2 = BOARD_HEIGHT;

    private static List<int[]> tempPath = null;     // TODO: remove tempPath when testing is done

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
        circle.add(Direction.UP);   // clockwise
        circle.add(Direction.RIGHT);
        circle.add(Direction.DOWN);
        circle.add(Direction.LEFT);
        return circle;
    }

    public static List<int[]> getPathSection(List<int[]> staticPath, List<int[]> snake, int[] goodie) {
        List<int[]> pathToGoodie = getPathBetween(staticPath, snake.get(0), goodie);
        printArray("pathToGoodie array: ", pathToGoodie);
        // get shortest path
        List<List<int[]>> pathWays = new ArrayList<>();
        collectPaths(pathWays, pathToGoodie, 0);
        Optional<List<int[]>> pathToGo = pathWays.stream().reduce((a, b) -> a.size()<b.size()? a:b);
        List<int[]> result = pathToGo.orElse(staticPath);

        // check if cutting off way out
        int threshold = (staticPath.size()-snake.size()) - (pathToGoodie.size()-result.size());  // area to move - skipped path
        if (threshold < snake.size()) {
            pathToGoodie.remove(0);

            return pathToGoodie;
        }
        printArray("result array: ", result);
        System.out.println("skipped: " + (pathToGoodie.size()-result.size()));
        return result;
    }

    private static void collectPaths(List<List<int[]>> pathWays, List<int[]> passedPath, int index) {
        List<int[]> path = new ArrayList<>(passedPath);
        int[] start = passedPath.get(index);

        int breakIndex = index;
        List<Integer> biList = new ArrayList<>();
        biList.add(breakIndex);
        boolean pathDone = false;
        for (int i = index; i < path.size(); i++) {
            if (i > index+1 && (intersect(path.get(i), start))) {
                breakIndex = i;
                biList.add(breakIndex);
                break;
            }
            if (i == path.size()-1) {
                pathDone = true;
            }
        }
        for (int bIndex : biList) {
            process(pathWays, new ArrayList<>(path), pathDone, index, bIndex);
        }
    }

    private static void process(List<List<int[]>> pathWays, List<int[]> path, boolean pathDone, int index, int breakIndex) {
        if (pathDone || path.size() <= 2) {
            if (path.size() > 1) {
                path.remove(0);
            }
            pathWays.add(path);
        } else {
            collectPaths(pathWays, new ArrayList<>(path), breakIndex);

            int counter = 0;
            int rem = breakIndex-index-1;
            if (rem > 0) {
                while (rem > 0) {
                    rem--;
                    counter++;
                    path.remove((index+1));
                }
            }
            breakIndex = breakIndex-counter-1;
            if (path.size() > 2 && counter != 0) {
                collectPaths(pathWays, new ArrayList<>(path), breakIndex);
            } else {
                if (path.size() > 1) {
                    path.remove(0);
                }
                pathWays.add(path);
            }
        }
    }

    private static List<int[]> getPathBetween(List<int[]> path, int[] start, int[] end) { // includes start, includes end
        List<int[]> pathSection = new ArrayList<>();
        boolean startFound = false;
        for (int i = 0; i < path.size(); i++) {
            if (!startFound && Arrays.equals(start, path.get(i))) {
                startFound = true;
            }
            if (startFound) {
                pathSection.add(path.get(i));
                if (Arrays.equals(path.get(i), end)) {
                    break;
                }
                if (i == path.size()-1) {
                    i = -1;
                }
            }
        }
        return pathSection;
    }


    public static List<int[]> getShortcut(List<int[]> currentPath, int[] start, int[] goodie) {     // TODO: stop with shortcuts when snake is too large
        List<int[]> path = new ArrayList<>(currentPath);
        path.add(0, start);

        int breakIndex = 0;
        for (int i = 0; i < path.size(); i++) {
            if (i > 1 && (intersect(path.get(i), start))) {     // TODO: target goodie?
                breakIndex = i;
                break;
            } else if (Arrays.equals(path.get(i), goodie)) {     // too late, abort
                break;
            }
        }

        do {
            path.remove(0);
            breakIndex--;
        } while (breakIndex > 0);

        return path;
    }

    public static List<int[]> getHamilton(int[] start) {    // TODO: backtracking may not be optimal yet
        if (tempPath != null) {
            return tempPath;
        }
        resetBoardDimensions();
        List<int[]> path = PathGenerator.getPathFromDirections(start, getStartCircle());
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
        tempPath = path;
        printArray("initial array: ", path);
        return path;
        //return tempPath;
    }

    private static void printArray(String name, List<int[]> list) {
        System.out.println(name + ": " + Arrays.deepToString(list.toArray()).replace("[", "{").replace("]", "}"));
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
