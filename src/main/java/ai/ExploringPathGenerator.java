package ai;



public class ExploringPathGenerator extends PathGenerator {
/*

    private static final int THRESHOLD = 50;

    public static List<Direction> getExploringPath(List<int[]> snake, int[] goodie) {
        List<Direction> path= new ArrayList<>();
        for (int i = 0; i < THRESHOLD; i++) {
            if (path.isEmpty()) {
                path = getExploring(snake, goodie);
            } else {
                break;
            }
        }

        if (path.isEmpty()) {

            Direction dir = Direction.getRandomDirection();
            path.add(dir);
        }
        return path;
    }

    private static List<Direction> getExploring(List<int[]> snake, int[] goodie) {
        List<Direction> path = new ArrayList<>();
        HashSet<Direction> dirsTested = new HashSet<>();
        List<int[]> fakeSnake = new ArrayList<>(snake);
        int[] current = snake.get(0);
        Direction dir = Direction.getRandomDirection();
        int[] next = new int[] {current[0]+dir.getX(), current[1]+dir.getY()};
        boolean goodieReached = false;
        while (!goodieReached) {
            if (!exists(fakeSnake, next) && isValid(next)) {
                path.add(0, dir);
                fakeSnake.add(0, next.clone());
                fakeSnake.remove(fakeSnake.size()-1);
                current = next.clone();
                goodieReached = Arrays.equals(goodie, current);
                dirsTested.clear();
            } else {
                dirsTested.add(dir);
                if (dirsTested.size() == 4) {
                    return new ArrayList<>();
                }
            }
            if (!goodieReached) {
                dir = Direction.getRandomDirection();
                next = new int[]{current[0] + dir.getX(), current[1] + dir.getY()};

            }
        }
        return path;
    }

 */
}
