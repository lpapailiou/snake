package neuralnet;

import ai.netadapter.Serializer;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SerializeTest {

    @Test
    public void seriTest() {
        NeuralNetwork net = new NeuralNetwork(12,2,4);
        String fileName = "src/main/resources/serialized/NeuralNet.ser";
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(net);
            out.close();
            file.close();

            System.out.println("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        NeuralNetwork phoenix = null;
        try {
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(file);

            phoenix = (NeuralNetwork) in.readObject();

            in.close();
            file.close();

            System.out.println("success? " + phoenix != null);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
*/

    }

}

