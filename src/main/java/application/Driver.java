package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import util.Direction;
import util.KeyParser;

public class Driver extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Driver.class.getClassLoader().getResource("GamePanel.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 500);
            scene.getStylesheets().add(Driver.class.getClassLoader().getResource("style.css").toExternalForm());
            //scene.setFill(COLOR_MODE.appBackground);
            scene.setFill(Color.BLACK);
            stage.setScene(scene);
            stage.setMinHeight(539);
            stage.setMinWidth(516);
            stage.setMaxHeight(539);
            stage.setMaxWidth(516);
            stage.setTitle("Snake");
            //stage.getIcons().add(new Image("snake.png"));
            stage.show();
            setUpKeyParser(scene);
            GamePanel.getPanel().setDimensions(scene.getWidth(), scene.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpKeyParser(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            Direction dir = KeyParser.handleKeyPress(e);
            if (dir.name().equals("GONE")) {
                GamePanel.terminate();
            } else if (dir.name().equals("NONE")) {
                GamePanel.restart();
            } else {
                GamePanel.setDirection(dir);
            }
            e.consume();
        });
    }
}
