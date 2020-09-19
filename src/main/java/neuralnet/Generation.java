package neuralnet;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Generation {

    int populationSize;
    HashMap<BoardAdapter, Integer> generations = new HashMap<>();

    public Generation(int populationSize) {
        this.populationSize = populationSize;
    }

    public NeuralNetwork run(NeuralNetwork net) {
        for (int i = 0; i < populationSize; i++) {
            BoardAdapter adapter = new BoardAdapter(i == 0 ? net : net.clone());
            boolean running = true;
            while (running) {
                running = adapter.moveSnake();
            }
            generations.put(adapter, adapter.getFitness());
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
}
