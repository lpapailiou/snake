package netadapter;

import neuralnet.NeuralNetwork;
import util.Setting;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Generation {

    private int populationSize;
    private final static int THREADPOOL = 200;
    private List<BoardAdapter> generations = new ArrayList<>();

    public Generation(int populationSize) {
        this.populationSize = populationSize;
    }

    public NeuralNetwork run(NeuralNetwork net) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADPOOL);

        for (int i = 0; i < populationSize; i++) {
            Runnable worker = new BackgroundGame(i == 0 ? net : net.clone(), generations);
            executor.execute(worker);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("executor service interrupted unexpectedly!");
        }

        return evolve(generations);
    }

    private NeuralNetwork evolve(List<BoardAdapter> generations) {
        generations.sort(Comparator.nullsLast(Collections.reverseOrder()));
        NeuralNetwork best = generations.get(0).getNet();
        System.out.println("fitness of generation is: " + generations.get(0).getFitness() + " \t snake length: " + generations.get(0).getSnakeLength());
        if (generations.get(0).getSnakeLength() < 10) {
            NeuralNetwork secondBest = generations.get(1).getNet();
            return NeuralNetwork.merge(best, secondBest);
        }


        // do roulette algorithm
        int bound = (int) (Setting.getSettings().getPopulationSize()*0.01);
        int choice = 2;

        Map<Integer, Long> map = new HashMap();
        double sum = 0;
        for (int i = 0; i < bound; i++) {
            BoardAdapter adaapter = generations.get(i);
            sum += adaapter.getFitness();
            map.put(i, adaapter.getFitness());
        }

        for (int i = 0; i < choice; i++) {
            best = NeuralNetwork.merge(best, spin(generations, map, bound, sum));
        }

        //NeuralNetwork secondBest = generations.get(1).getNet();
        //return NeuralNetwork.merge(best, secondBest);
        return best;
    }

    private NeuralNetwork spin(List<BoardAdapter> generations, Map<Integer, Long> map, int bound, double sum) {
        long checksum = 0;
        NeuralNetwork chosen = null;
        for (int i = 0; i < bound; i++) {
            checksum += map.get(i);
            if (checksum > new Random().nextInt((int) sum)) {
                chosen = generations.get(i).getNet();
                break;
            }
        }
        return chosen;
    }

    static class BackgroundGame implements Runnable {
        NeuralNetwork net;
        List<BoardAdapter> gen;
        BackgroundGame(NeuralNetwork net, List<BoardAdapter> generation) {
            this.net = net;
            this.gen = generation;
        }

        @Override
        public void run() {
            BoardAdapter adapter = new BoardAdapter(net);
            boolean running = true;
            while (running) {
                running = adapter.moveSnake();
            }
            gen.add(adapter);
        }
    }
}
