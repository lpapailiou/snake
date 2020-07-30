package application;

import ai.bot.HamiltonianBot;
import game.Board;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import util.Direction;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static util.Setting.*;

public class GamePanel implements Initializable {

    @FXML
    private Pane gamePane;

    @FXML
    private Label gameOverTitle;

    @FXML
    private Label gameOverText;

    private static GamePanel instance;
    private GraphicsContext context;
    private Board board = new Board();
    private int CELL_WIDTH = 10;
    private int STROKE_WIDTH = 5;
    double WINDOW_WIDTH;
    double WINDOW_HEIGHT;
    int BASE_PADDING = 20;
    int PADDING_WIDTH = BASE_PADDING;
    int PADDING_HEIGHT = BASE_PADDING;
    private FadeTransition transitionGameOverTitle = null;
    private FadeTransition transitionGameOverText = null;
    private Direction direction = Direction.getRandomDirection();
    private boolean isTimerStopped = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        gameOverTitle.setTextFill(COLOR_SCHEME.getText());
        gameOverText.setTextFill(COLOR_SCHEME.getText());
    }

    private void setUpTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(SPEED), event -> {
            if (!isTimerStopped) {
                move(direction);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static GamePanel getPanel() {
        return instance;
    }

    public List<int[]> getSnake() {
        return board.getSnake();
    }

    public int[] getGoodie() {
        return board.getGoodie();
    }

    public static void setDirection(Direction dir) {
        instance.direction = dir;
    }

    public void setDimensions(double width, double height) {
        WINDOW_WIDTH = width;
        WINDOW_HEIGHT = height;
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        context = canvas.getGraphicsContext2D();
        gamePane.getChildren().add(canvas);

        if (BOARD_WIDTH >= BOARD_HEIGHT) {
            CELL_WIDTH = (int) (WINDOW_WIDTH-BASE_PADDING*2)/BOARD_WIDTH;
        } else {
            CELL_WIDTH = (int) (WINDOW_HEIGHT-BASE_PADDING*2)/BOARD_HEIGHT;
        }
        STROKE_WIDTH = CELL_WIDTH/5*4;
        PADDING_WIDTH = (int) (WINDOW_WIDTH-(CELL_WIDTH*BOARD_WIDTH))/2;
        PADDING_HEIGHT = (int) (WINDOW_HEIGHT-(CELL_WIDTH*BOARD_HEIGHT))/2;

        paint();
        if (!HASBOT) {
            setUpTimer();
        } else {
            setUpBot();
        }
    }

    public static boolean move(Direction dir) {
        instance.direction = dir;
        boolean success = instance.board.moveSnake(dir);
        handleStep(success);
        return success;
    }

    public static boolean move(int[] coord) {
        boolean success = instance.board.moveSnake(coord);
        handleStep(success);
        return success;
    }

    private static void handleStep(boolean success) {
        if (success) {
            instance.paint();
        } else {
            instance.isTimerStopped = true;
            instance.repaint();
            //instance.showEndGameDialog();
        }
    }

    private void showEndGameDialog() {
        paintGameOverBackground();
        String gameResult = (board.getResult() == -1) ? "GAME OVER!": "YOU WIN!";
        gameOverTitle.setText(gameResult);
        gameOverText.setText("try again? (y / n)");
        transitionGameOverTitle = animateGameOverDialog(gameOverTitle);
        transitionGameOverText = animateGameOverDialog(gameOverText);
    }

    private FadeTransition animateGameOverDialog(Label label) {
        FadeTransition transition = new FadeTransition(Duration.millis(700), label);
        transition.setFromValue(1.0);
        transition.setToValue(0.0);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.play();
        return transition;
    }

    private void stopGameOverAnimation() {
        gameOverTitle.setText("");
        gameOverText.setText("");
        if (instance.transitionGameOverTitle != null && instance.transitionGameOverText != null) {
            instance.transitionGameOverTitle.stop();
            instance.transitionGameOverText.stop();
        }
    }

    public static void restart() {
        if (instance.board.isGameFinished()) {
            instance.stopGameOverAnimation();
            instance.board = new Board();
            instance.paint();
            instance.direction = Direction.getRandomDirection();
            instance.isTimerStopped = false;
            setUpBot();
        }
    }

    private static void setUpBot() {
        if (HASBOT) {
            new HamiltonianBot().start();
        }
    }

    public static void terminate() {
        if (instance.board.isGameFinished()) {
            try {
                Platform.exit();
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void paint() {
        paintBackground(COLOR_SCHEME.getBackgroundFrame());
        paintSnake();
        paintGoodie();
    }

    private void repaint() {
        paintBackground(COLOR_SCHEME.getBackgroundFrameEnd());
        paintSnake();
        paintGoodie();
    }

    private void paintBackground(Color backgroundFrameColor) {
        context.clearRect(0, 0, BOARD_WIDTH*CELL_WIDTH, BOARD_HEIGHT*CELL_WIDTH);
        int offset = (Math.min(PADDING_WIDTH, PADDING_HEIGHT))/6;
        context.setFill(backgroundFrameColor);
        context.fillRect(PADDING_WIDTH-offset, PADDING_HEIGHT-offset, (CELL_WIDTH*BOARD_WIDTH)+offset*2, (CELL_WIDTH*BOARD_HEIGHT)+offset*2);
        context.setFill(COLOR_SCHEME.getBackground());
        context.fillRect(PADDING_WIDTH, PADDING_HEIGHT, (CELL_WIDTH*BOARD_WIDTH), (CELL_WIDTH*BOARD_HEIGHT));
    }

    private void paintGameOverBackground() {
        context.setFill(COLOR_SCHEME.getBackground());
        context.fillRect(PADDING_WIDTH, WINDOW_HEIGHT/2-80, (CELL_WIDTH*BOARD_WIDTH), 110);
    }

    private void paintGoodie() {
        int[] goodie = board.getGoodie();
        drawCell(goodie[0], goodie[1], COLOR_SCHEME.getGoodie());
    }

    private void paintSnake() {
        List<int[]> snake = board.getSnake();
        if (snake.size() > 1) {
            for (int i = 0; i < snake.size() - 1; i++) {
                int[] part = snake.get(i);
                int[] partNext;
                if (i + 1 < snake.size()) {
                    partNext = snake.get(i + 1);
                } else {
                    partNext = snake.get(i);
                }
                drawLine(part[0], part[1], partNext[0], partNext[1], COLOR_SCHEME.getSnake());
            }
        } else {
            drawCell(snake.get(0)[0], snake.get(0)[1], COLOR_SCHEME.getSnake());
        }
    }

    private void drawLine(int x1, int y1, int x2, int y2, Color color) {
        context.setStroke(color);
        context.setLineWidth(STROKE_WIDTH);
        context.strokeLine(x1*CELL_WIDTH+CELL_WIDTH/2+PADDING_WIDTH, y1*CELL_WIDTH+CELL_WIDTH/2+PADDING_HEIGHT, x2*CELL_WIDTH+CELL_WIDTH/2+PADDING_WIDTH, y2*CELL_WIDTH+CELL_WIDTH/2+PADDING_HEIGHT);
    }

    private void drawCell(int x, int y, Color color) {
        context.setFill(color);
        context.fillRect(x*CELL_WIDTH+((CELL_WIDTH-STROKE_WIDTH)/2)+PADDING_WIDTH, y*CELL_WIDTH+((CELL_WIDTH-STROKE_WIDTH)/2)+PADDING_HEIGHT, STROKE_WIDTH, STROKE_WIDTH);
    }
}
