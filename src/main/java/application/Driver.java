package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import util.Direction;
import util.KeyParser;

import static util.Setting.COLOR_SCHEME;
import static util.Setting.HASBOT;

public class Driver extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Driver.class.getClassLoader().getResource("GamePanel.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 500);
            scene.getStylesheets().add(Driver.class.getClassLoader().getResource("style.css").toExternalForm());
            scene.setFill(COLOR_SCHEME.getBackground());
            root.setStyle("-fx-text-fill: red;");
            stage.setScene(scene);
            stage.setMinHeight(539);
            //stage.setMinWidth(516);
            stage.setMinWidth(1016);
            stage.setMaxHeight(539);
            //stage.setMaxWidth(516);
            stage.setMaxWidth(1016);
            stage.setTitle("Snake");
            stage.getIcons().add(new Image("snake.png"));
            setUpKeyParser(scene);
            stage.show();
            //GamePanel.getPanel().setDimensions(scene.getWidth(), scene.getHeight());
            GamePanel.getPanel().setDimensions(500, 500);

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
                if (!HASBOT) {
                    GamePanel.setDirection(dir);
                }
            }
            e.consume();
        });
    }
}
