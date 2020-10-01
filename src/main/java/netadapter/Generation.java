package netadapter;

import neuralnet.NeuralNetwork;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Generation {

    private int populationSize;
    private final static int THREADPOOL = 200;
    private HashMap<BoardAdapter, Integer> generations = new HashMap<>();

    public Generation(int populationSize) {
        this.populationSize = populationSize;
    }

    public NeuralNetwork run(NeuralNetwork net) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADPOOL);

        for (int i = 0; i < populationSize; i++) {
            Runnable worker = new BackgroundGame(i == 0 ? net : net.clone(), generations);
            executor.execute(worker);


            /*BoardAdapter adapter = new BoardAdapter(i == 0 ? net : net.clone());
            boolean running = true;
            while (running) {
                running = adapter.moveSnake();
            }
            generations.put(adapter, adapter.getFitness());*/
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("executor service interrupted unexpectedly!");
        }
        NeuralNetwork best = getBest(generations);
        if (generations.isEmpty()) {
            return best;
        }
        NeuralNetwork secondBest = getBest(generations);
        return NeuralNetwork.merge(best, secondBest);
    }

    private NeuralNetwork getBest(HashMap<BoardAdapter, Integer> map) {
        int max = Collections.max(generations.values());
        List<BoardAdapter> ad = map.entrySet().stream().filter(e -> e.getValue() == max).map(Map.Entry::getKey).collect(Collectors.toList());
        if (!ad.isEmpty()) {
            System.out.println("----------------------------------------------------------fitness of generation is: " + ad.get(0).getFitness());
            map.remove(ad.get(0));
        }
        return ad.isEmpty() ? null : ad.get(0).getNet();
    }

    static class BackgroundGame implements Runnable {
        NeuralNetwork net;
        HashMap<BoardAdapter, Integer> gen;
        BackgroundGame(NeuralNetwork net, HashMap<BoardAdapter, Integer> generation) {
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
            gen.put(adapter, adapter.getFitness());
        }
    }
}
