package netadapter;

import neuralnet.NeuralNetwork;

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
        NeuralNetwork secondBest = generations.get(1).getNet();


        /*  // TODO: improve or remove
        // merge together some of the top scorers
        for (int i = 0; i < bound; i++) {
          best = NeuralNetwork.merge(best, populationList.get(random.nextInt(bound)).getNeuralNetwork());
        }

        // seed in some random scorers to break 'eaten in' patterns and avoid local maima
        for (int i = 0; i < bound/2; i++) {
          best = NeuralNetwork.merge(best, populationList.get(random.nextInt(populationSize)).getNeuralNetwork());
        }
        */

        return NeuralNetwork.merge(best, secondBest);
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
