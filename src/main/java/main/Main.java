package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            FXMLLoader loader = new FXMLLoader(classLoader.getResource("ApplicationPanel.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 500);
            scene.getStylesheets().add(Main.class.getClassLoader().getResource("application.css").toExternalForm());
            scene.getStylesheets().add(Main.class.getClassLoader().getResource(Config.getInstance().getTheme().getCss()).toExternalForm());
            scene.setFill(Config.getInstance().getTheme().getBackgroundColor());
            stage.setScene(scene);
            stage.setMinHeight(839);    //539
            stage.setMinWidth(1616);
            stage.setMaxHeight(839);
            stage.setMaxWidth(1616);
            stage.setTitle("Snake");
            stage.getIcons().add(new Image("snake.png"));
            //setUpKeyParser(scene);
            stage.show();
            //GamePanel.getPanel().setDimensions();
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.getClass());
            e.printStackTrace();
        }
    }
/*
    private void setUpKeyParser(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            Direction dir = KeyParser.handleKeyPress(e);
            if (dir.name().equals("GONE")) {
                //GamePanel.terminate();
            } else if (dir.name().equals("NONE")) {
                //GamePanel.restart();
            } else {
                if (!Setting.getSettings().hasBot()) {
                    //GamePanel.setDirection(dir);
                }
            }
            e.consume();
        });
    }*/
}
