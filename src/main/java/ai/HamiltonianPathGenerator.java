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
    private static int PREV_SNAKE_SIZE = 0;

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
        boardX1 = (boardX1 > 2) ? boardX1-2 : 0;
        boardY1 = (boardY1 > 2) ? boardY1-2 : 0;
        boardX2 = (boardX2 < BOARD_WIDTH-1) ? boardX2+2 : BOARD_WIDTH;
        boardY2 = (boardY2 < BOARD_HEIGHT-1) ? boardY2+2 : BOARD_HEIGHT;
    }

    private static void decrementBoardDimensions() {
        boardX1 += 2;
        boardY1 += 2;
        boardX2 -= 2;
        boardY2 -= 2;
    }

    // Todo: A* algorithm for shortest path

    public static List<Direction> getStartCircle() {
        List<Direction> circle = new ArrayList();
        circle.add(Direction.UP);   // clockwise
        circle.add(Direction.RIGHT);
        circle.add(Direction.DOWN);
        circle.add(Direction.LEFT);
        return circle;
    }

    public static List<int[]> getPathSection(List<int[]> staticPath, List<int[]> snake, int[] goodie) {
        System.out.println("snake: "+ Arrays.toString(snake.get(0)) +", goodie: "+ Arrays.toString(goodie));
        List<int[]> pathToGoodie = getPathBetween(staticPath, snake.get(0), goodie);
        printArray("pathToGoodie array: (size " + pathToGoodie.size() + ") ", pathToGoodie, true);


        List<List<int[]>> pathWays = new ArrayList<>();
        boolean full = false;
        if (pathToGoodie.size() < 300) {
            full = true;
        }
        collectPaths(pathWays, new ArrayList<>(pathToGoodie), 0, full);
        Collections.sort(pathWays, (Comparator<List>) (a1, a2) -> {
            return a1.size() - a2.size();
        });

        List<int[]> result = new ArrayList<>();

        int realSnakeSize = getSnakeSize(staticPath, snake);

        boolean success = false;
        for (List<int[]> pathToCheck : pathWays) {
            if (pathToCheck.size() > 0) {
                pathToCheck.remove(0);
            }

            int checkInt = staticPath.size() - realSnakeSize - (pathToGoodie.size() - pathToCheck.size())-1;
            success = (checkInt > 0);
            if (success) {
                result = pathToCheck;
                break;
            }
        }

        if (!success) {
            if (pathToGoodie.size() > 0) {
                pathToGoodie.remove(0);
            }
            System.out.println("ORIGINAL ARRAY RETURNED\n");
            return pathToGoodie;
        }
        printArray("shortcut array: (size " + result.size() + ") ", result, false);
        System.out.println("SHORTCUT ARRAY RETURNED\n");
        return result;
    }

    private static int getSnakeSize(List<int[]> staticPath, List<int[]> snake) {
        int[] start = snake.get(snake.size()-1);
        int[] end = snake.get(0);
        int size = 0;
        boolean startFound = false;
        for (int i = 0; i < staticPath.size(); i++) {
            if (!startFound && !startFound && Arrays.equals(staticPath.get(i), start)) {
                startFound = true;
            }
            if (startFound) {
                size++;
                if (Arrays.equals(staticPath.get(i), end)) {

                    break;
                }
            }
            if (i == staticPath.size()-1) {
                i = -1;
            }
        }
        return size;
    }

    private static void collectPaths(List<List<int[]>> pathWays, List<int[]> passedPath, int index, boolean full) {
        if (passedPath.size() == (index+1)) {
            if (!pathWays.contains(passedPath)) {
                pathWays.add(passedPath);
            }
            return;
        }
        List<int[]> path = new ArrayList<>(passedPath);
        int[] start = passedPath.get(index);

        int breakIndex = index;
        int breakIndex2 = index;
        boolean pathDone = false;
        for (int i = index; i < path.size(); i++) {
            if (i > index+1 && (intersect(path.get(i), start))) {
                if (breakIndex == index) {
                    breakIndex = i;
                } else {
                    breakIndex2 = i;
                    break;
                }
            }
            if (i == path.size()-1) {
                pathDone = true;
            }
        }
        if ((breakIndex+1) < path.size() && full) {
            collectPaths(pathWays, new ArrayList<>(path), breakIndex + 1, full);        // continue
        }
        if (full) {
            if (breakIndex != index) {
                process(pathWays, new ArrayList<>(path), pathDone, index, breakIndex, full);     // cut first section
            }
        }
        if (breakIndex2 != index || !full) {
            if (!full) {
                if (breakIndex2 == index){
                    breakIndex2 = breakIndex;
                }
            }
            if (breakIndex2 != index) {
                process(pathWays, new ArrayList<>(path), pathDone, index, breakIndex2, full); // cut second section
            }
        }
    }

    private static void process(List<List<int[]>> pathWays, List<int[]> path, boolean pathDone, int index, int breakIndex, boolean full) {
        if (breakIndex==index || path.size() <= 2 || index == path.size()-1) {
            if (!pathWays.contains(path)) {
                pathWays.add(path);
            }
            return;
        } else {
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
            if (path.size() > 2) {
                collectPaths(pathWays, new ArrayList<>(path), breakIndex+1, full);        // continue
            } else {
                if (!pathWays.contains(path)) {
                    pathWays.add(path);
                }
                return;
            }
        }
    }

    public static List<int[]> getPathBetween(List<int[]> path, int[] start, int[] end) { // includes start, includes end
        List<int[]> pathSection = new ArrayList<>();
        boolean startFound = false;
        boolean secondRound = false;
        for (int i = 0; i < path.size(); i++) {
            if (!startFound && Arrays.equals(start, path.get(i))) {
                startFound = true;
            }
            if (startFound) {
                pathSection.add(path.get(i));
                if (Arrays.equals(path.get(i), end)) {
                    break;
                }
                if (i == path.size()-1 && !secondRound) {       // check in case board cells are not even
                    i = -1;
                    secondRound = true;
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
        PREV_SNAKE_SIZE = 1;
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
        //initial array: : {{4, 5}, {4, 6}, {4, 7}, {3, 7}, {2, 7}, {2, 8}, {2, 9}, {1, 9}, {0, 9}, {0, 8}, {1, 8}, {1, 7}, {0, 7}, {0, 6}, {1, 6}, {1, 5}, {0, 5}, {0, 4}, {1, 4}, {2, 4}, {2, 5}, {2, 6}, {3, 6}, {3, 5}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, {6, 3}, {5, 3}, {4, 3}, {3, 3}, {2, 3}, {1, 3}, {0, 3}, {0, 2}, {1, 2}, {2, 2}, {2, 1}, {1, 1}, {0, 1}, {0, 0}, {1, 0}, {2, 0}, {3, 0}, {3, 1}, {3, 2}, {4, 2}, {5, 2}, {5, 1}, {4, 1}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {8, 0}, {9, 0}, {9, 1}, {8, 1}, {7, 1}, {6, 1}, {6, 2}, {7, 2}, {8, 2}, {9, 2}, {9, 3}, {8, 3}, {7, 3}, {7, 4}, {8, 4}, {9, 4}, {9, 5}, {8, 5}, {8, 6}, {9, 6}, {9, 7}, {8, 7}, {8, 8}, {9, 8}, {9, 9}, {8, 9}, {7, 9}, {7, 8}, {7, 7}, {7, 6}, {7, 5}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {5, 9}, {4, 9}, {3, 9}, {3, 8}, {4, 8}, {5, 8}, {5, 7}, {5, 6}, {5, 5}};

        tempPath = path;
        printArray("initial array: ", path, false);
        return path;
        //return tempPath;
    }

    private static void printArray(String name, List<int[]> list, boolean removeFirst) {
        if (removeFirst && list.size() > 11) {
            String arr = Arrays.deepToString(list.toArray()).replace("[", "{").replace("]", "}");
            arr = "{"+arr.substring(11);
            System.out.println(name + ": " + arr);
        } else {
            System.out.println(name + ": " + Arrays.deepToString(list.toArray()).replace("[", "{").replace("]", "}"));
        }
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
