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
import util.Setting;

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
            scene.getStylesheets().add(Driver.class.getClassLoader().getResource("application.css").toExternalForm());
            scene.getStylesheets().add(Driver.class.getClassLoader().getResource(Setting.getSettings().getTheme().getCss()).toExternalForm());
            scene.setFill(Setting.getSettings().getTheme().getBackgroundColor());
            stage.setScene(scene);
            stage.setMinHeight(839);    //539
            stage.setMinWidth(1616);
            stage.setMaxHeight(839);
            stage.setMaxWidth(1616);
            stage.setTitle("Snake");
            stage.getIcons().add(new Image("snake.png"));
            setUpKeyParser(scene);
            stage.show();
            GamePanel.getPanel().setDimensions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpKeyParser(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            Direction dir = KeyParser.handleKeyPress(e);
            if (dir.name().equals("GONE")) {
                //GamePanel.terminate();
            } else if (dir.name().equals("NONE")) {
                //GamePanel.restart();
            } else {
                if (!Setting.getSettings().hasBot()) {
                    GamePanel.setDirection(dir);
                }
            }
            e.consume();
        });
    }
}
