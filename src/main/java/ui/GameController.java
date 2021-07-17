package ui;

import game.Game;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import ui.util.GamePainter;

import java.net.URL;
import java.util.ResourceBundle;


public class GameController implements Initializable {

  @FXML
  private Canvas gamePane;

  private GamePainter gamePainter;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    resetGameDisplay();
  }

  void display(Game game) {
    boolean isActive = game == null || game.getRealSnake().isAlive();
    if (game == null) {
      gamePainter.paintBoard(true);
    } else {
      gamePainter.paintBoard(isActive);
      gamePainter.paintFood(game.getGoodie());
      gamePainter.paintSnake(game.getRealSnake());
    }
  }

  void resetGameDisplay() {
    gamePainter = new GamePainter(gamePane.getGraphicsContext2D());
    display(null);
  }

}
