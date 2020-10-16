package ai.netadapter;

import neuralnet.NeuralNetwork;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Serializer {

    public static void save(NeuralNetwork net) {
        String fileName = "src/main/resources/serialized/NeuralNet" + System.currentTimeMillis() + ".ser";
        try {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(net);
            out.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NeuralNetwork load() {
        String filename = "NeuralNet_16x16_12_12.ser";
        File tempfile = createTempFile("serialized/"+filename, filename);

        NeuralNetwork phoenix = null;
        try {
            InputStream inputStrean = new FileInputStream(tempfile);
            ObjectInputStream in = new ObjectInputStream(inputStrean);
            phoenix = (NeuralNetwork) in.readObject();

            inputStrean.close();
            in.close();

            //System.out.println("success? " + phoenix != null);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return phoenix;
    }

    private static File createTempFile(String resource, String fileName) {
        try {
            InputStream htmlFile = Serializer.class.getClassLoader().getResourceAsStream(resource);
            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
            Path tempPath = tempFile.toPath();
            Files.copy(htmlFile, tempPath, StandardCopyOption.REPLACE_EXISTING);
            //tempFile.deleteOnExit();
            //URI url = tempFile.toURI();
            //Desktop.getDesktop().browse(url);
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("fail");
    }
}
