package ai;

import game.Board;
import geneticalgorithm.GeneticAlgorithmObject;
import neuralnet.NeuralNetwork;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AdapterTemplateTest {

    @Test
    public void test() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> templateBuilder= Class.forName("ai.netadapter.BoardDecorator").getConstructor(String.class);
        GeneticAlgorithmObject object = (GeneticAlgorithmObject) templateBuilder.newInstance(new Object[] {new Board(), new NeuralNetwork(2,2,2)});
    }
}
