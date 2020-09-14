package application;

import ai.bot.Bot;
import ai.bot.DeepBot;
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
import util.ColorScheme;
import util.Direction;
import util.Setting;

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
    private double CELL_WIDTH = 10;
    private double STROKE_WIDTH = 5;
    private double WINDOW_WIDTH;
    private double WINDOW_HEIGHT;
    private double BASE_PADDING = 20;
    private double PADDING_WIDTH = BASE_PADDING;
    private double PADDING_HEIGHT = BASE_PADDING;
    private FadeTransition transitionGameOverTitle = null;
    private FadeTransition transitionGameOverText = null;
    private Direction direction = Direction.getRandomDirection();
    private boolean isTimerStopped = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        gameOverTitle.setTextFill(Setting.getSettings().getColorScheme().getText());
        gameOverText.setTextFill(Setting.getSettings().getColorScheme().getText());
    }

    private void setUpTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(Setting.getSettings().getSpeed()), event -> {
            if (!isTimerStopped && !Setting.getSettings().hasBot()) {
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

    public void setDimensions() {
        WINDOW_WIDTH = 500;
        WINDOW_HEIGHT = 500;
        int width = Setting.getSettings().getBoardWidth();
        int height = Setting.getSettings().getBoardHeight();
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        context = canvas.getGraphicsContext2D();
        gamePane.getChildren().add(canvas);

        if (width >= height) {
            CELL_WIDTH = (WINDOW_WIDTH-BASE_PADDING*2)/width;
        } else {
            CELL_WIDTH = (WINDOW_HEIGHT-BASE_PADDING*2)/height;
        }
        STROKE_WIDTH = CELL_WIDTH/5*4;
        if (STROKE_WIDTH < 2) {
            STROKE_WIDTH = 2;
        }
        PADDING_WIDTH = (int) (WINDOW_WIDTH-(CELL_WIDTH*width))/2;
        PADDING_HEIGHT = (int) (WINDOW_HEIGHT-(CELL_WIDTH*height))/2;
        fitBoard();
        paint();
    }

    public void startBot() {
        ConfigPanel.getPanel().lockInput(true);
        if (!Setting.getSettings().hasBot()) {
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

    public static Board getBoard() {
        if (instance != null) {
            return instance.board;
        }
        return new Board();
    }

    public static boolean move(int[] coord) {
        boolean success = instance.board.moveSnake(coord);
        handleStep(success);
        return success;
    }

    public static boolean move(int[] coord, List<int[]> goodiepath, List<int[]> path) {
        boolean success = instance.board.moveSnake(coord);
        handleStep(success, goodiepath, path);
        return success;
    }

    private static void handleStep(boolean success, List<int[]> goodiepath, List<int[]> path) {
        if (success) {
            instance.paint(goodiepath, path);
        } else {
            instance.isTimerStopped = true;
            instance.repaint();
            //instance.showEndGameDialog();
        }
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
            instance.startBot();
        }
    }

    public static void prepareNextGeneration() {
        ConfigPanel.getPanel().lockInput(true);
        instance.board = new Board();
        instance.paint();
        instance.isTimerStopped = false;
    }

    private static void setUpBot() {
        if (Setting.getSettings().hasBot()) {
            Setting.getSettings().getBot().start();
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

    public void fitBoard() {
        board = new Board();
        paint();
    }

    public void paint() {
        paintBackground(Setting.getSettings().getColorScheme().getBackgroundFrame());
        paintSnake();
        paintGoodie();
    }

    private void paint(List<int[]> pathgoodie, List<int[]> path) {
        paintBackground(Setting.getSettings().getColorScheme().getBackgroundFrame());
        paintPath(pathgoodie, Color.BLUE);
        paintPath(path, Color.YELLOW);
        paintSnake();
        paintGoodie();
    }



    private void repaint() {
        paintBackground(Setting.getSettings().getColorScheme().getBackgroundFrameEnd());
        paintSnake();
        paintGoodie();
        ConfigPanel.getPanel().lockInput(false);
    }

    private void paintBackground(Color backgroundFrameColor) {
        int width = Setting.getSettings().getBoardWidth();
        int height = Setting.getSettings().getBoardHeight();
        context.clearRect(0, 0, 500, 500);
        context.setFill(Setting.getSettings().getColorScheme().getBackground());
        context.fillRect(0, 0, 500, 500);
        //int offset = (Math.min(PADDING_WIDTH, PADDING_HEIGHT))/6;
        int offset = 3;
        context.setFill(backgroundFrameColor);
        context.fillRect(PADDING_WIDTH-offset, PADDING_HEIGHT-offset, (CELL_WIDTH*width)+offset*2, (CELL_WIDTH*height)+offset*2);
        context.setFill(Setting.getSettings().getColorScheme().getBackground());
        context.fillRect(PADDING_WIDTH, PADDING_HEIGHT, (CELL_WIDTH*width), (CELL_WIDTH*height));
    }

    private void paintGameOverBackground() {
        context.setFill(Setting.getSettings().getColorScheme().getBackground());
        context.fillRect(PADDING_WIDTH, WINDOW_HEIGHT/2-80, (CELL_WIDTH*Setting.getSettings().getBoardWidth()), 110);
    }

    private void paintGoodie() {
        int[] goodie = board.getGoodie();
        drawCell(goodie[0], goodie[1], Setting.getSettings().getColorScheme().getGoodie());
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
                drawLine(part[0], part[1], partNext[0], partNext[1], Setting.getSettings().getColorScheme().getSnake());
            }
        } else {
            drawCell(snake.get(0)[0], snake.get(0)[1], Setting.getSettings().getColorScheme().getSnake());
        }
    }

    private void paintPath(List<int[]> path, Color color) {
        if (path.size() > 1) {
            for (int i = 0; i < path.size() - 1; i++) {
                int[] part = path.get(i);
                int[] partNext;
                if (i + 1 < path.size()) {
                    partNext = path.get(i + 1);
                } else {
                    partNext = path.get(i);
                }
                drawLine(part[0], part[1], partNext[0], partNext[1], color);
            }
        } else {
            drawCell(path.get(0)[0], path.get(0)[1], color);
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
