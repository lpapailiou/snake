package ui.util;

import game.Snake;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Config;
import main.Theme;

import java.util.List;

public class GamePainter {

  private GraphicsContext context;
  private Config config = Config.getInstance();
  private Theme colors = config.getTheme();

  private static final double CANVAS_WIDTH = 800;     // must match fxml
  private double cellWidth;
  private double strokeWidth;
  private double paddingWidth;
  private double paddingHeight;

  public GamePainter(GraphicsContext context) {
    this.context = context;
    initializeDimensions();
  }

  public void paintSnake(Snake snake) {
    List<int[]> body = snake.getBody();

    if (body.size() <= 1) {
      drawCell(body.get(0)[0], body.get(0)[1], colors.getSnakeBodyColor());
      return;
    }

    for (int i = 0; i < body.size() - 1; i++) {
      int[] part = body.get(i);
      int[] partNext;
      if (i + 1 < body.size()) {
        partNext = body.get(i + 1);
      } else {
        partNext = body.get(i);
      }
      drawLine(part[0], part[1], partNext[0], partNext[1], colors.getSnakeBodyColor());
    }
  }

  public void paintFood(int[] foodPosition) {
    if (foodPosition != null) {
      drawCell(foodPosition[0], foodPosition[1], colors.getFoodColor());
    }
  }

  public void paintBoard(boolean isActive) {
    int width = config.getBoardWidth();
    int height = config.getBoardHeight();
    context.clearRect(0, 0, CANVAS_WIDTH, CANVAS_WIDTH);
    context.setFill(colors.getBackgroundColor());
    context.fillRect(0, 0, CANVAS_WIDTH, CANVAS_WIDTH);
    double offset = (Math.min(paddingWidth, paddingHeight)) / 6;
    context.setFill(isActive ? colors.getFrameActiveColor() : colors.getFrameInactiveColor());
    context
        .fillRect(paddingWidth - offset, paddingHeight - offset, (cellWidth * width) + offset * 2,
            (cellWidth * height) + offset * 2);
    context.setFill(colors.getBackgroundColor());
    context.fillRect(paddingWidth, paddingHeight, (cellWidth * width), (cellWidth * height));
  }

  private void drawLine(int x1, int y1, int x2, int y2, Color color) {
    context.setStroke(color);
    context.setLineWidth(strokeWidth);
    context.strokeLine(x1 * cellWidth + cellWidth / 2 + paddingWidth,
        y1 * cellWidth + cellWidth / 2 + paddingHeight,
        x2 * cellWidth + cellWidth / 2 + paddingWidth,
        y2 * cellWidth + cellWidth / 2 + paddingHeight);
  }

  private void drawCell(int x, int y, Color color) {
    context.setFill(color);
    context.fillRect(x * cellWidth + ((cellWidth - strokeWidth) / 2) + paddingWidth,
        y * cellWidth + ((cellWidth - strokeWidth) / 2) + paddingHeight, strokeWidth, strokeWidth);
  }

  private void initializeDimensions() {
    double basePadding = 20;
    int width = config.getBoardWidth();
    int height = config.getBoardHeight();

    if (width >= height) {
      cellWidth = (CANVAS_WIDTH - basePadding * 2) / width;
    } else {
      cellWidth = (CANVAS_WIDTH - basePadding * 2) / height;
    }
    strokeWidth = cellWidth / 5 * 4;
    if (strokeWidth < 2) {
      strokeWidth = 2;
    }
    paddingWidth = (CANVAS_WIDTH - (cellWidth * width)) / 2;
    paddingHeight = (CANVAS_WIDTH - (cellWidth * height)) / 2;
  }

}
