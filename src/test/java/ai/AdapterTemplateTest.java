package ai;

import game.Game;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AdapterTemplateTest {

    @Test
    public void test() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //Constructor<?> templateBuilder= Class.forName("ai.netadapter.GameDecorator").getConstructor(String.class);
        //GeneticAlgorithmObject object = (GeneticAlgorithmObject) templateBuilder.newInstance(new Object[] {new Game(), new NeuralNetwork(2,2,2)});
    }
}
