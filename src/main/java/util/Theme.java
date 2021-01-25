package util;

import javafx.scene.paint.Color;

/**
 * This enum maps available themes to color patterns.
 */
public enum Theme {

  CLASSIC(Color.LIME, Color.LIME, Color.RED, Color.RED, Color.BLACK, true, "themeClassic.css"),
  SANDY(Color.web("#93826c"), Color.web("#93826c"), Color.web("#85a39f"), Color.web("#85a39f"),
      Color.web("#E7D1B5"), true, "themeSandy.css"),
  SUNSET(Color.web("#ff7582"), Color.web("#ff7582"), Color.web("#c56d86"), Color.MOCCASIN,
      Color.web("#2a3950"), true, "themeSunset.css"),
  CANDY(Color.web("#ffa7a7"), Color.web("#ffa7a7"), Color.web("#ffcbcb"), Color.MOCCASIN,
      Color.web("#c9fdff"), true, "themeCandy.css"),
  MINIMAL(Color.BLACK, Color.DARKGRAY, Color.MEDIUMVIOLETRED, Color.MEDIUMVIOLETRED, Color.WHITE,
      false, "themeMinimal.css"),
  YB(Color.web("#F9CC11"), Color.web("#F9CC11"), Color.web("#F9CC11"), Color.web("#ffffff"),
      Color.BLACK, true, "themeYoungBoys.css");

  private Color snakeBodyColor;
  private Color frameActiveColor;
  private Color frameInactiveColor;
  private Color foodColor;
  private Color backgroundColor;
  private boolean darkTheme;
  private String css;

  Theme(Color snakeBodyColor, Color frameActiveColor, Color frameInactiveColor, Color foodColor,
        Color backgroundColor, boolean darkTheme, String css) {
    this.snakeBodyColor = snakeBodyColor;
    this.frameActiveColor = frameActiveColor;
    this.frameInactiveColor = frameInactiveColor;
    this.foodColor = foodColor;
    this.backgroundColor = backgroundColor;
    this.darkTheme = darkTheme;
    this.css = css;
  }

  /**
   * Returns the color of the visualized snake.
   *
   * @return the snake color
   */
  public Color getSnakeBodyColor() {
    return snakeBodyColor;
  }

  /**
   * Returns the color of the game frame of an active game.
   *
   * @return the default frame color
   */
  public Color getFrameActiveColor() {
    return frameActiveColor;
  }

  /**
   * Returns the color of the game frame when a game over occurred.
   *
   * @return the game-over-frame color
   */
  public Color getFrameInactiveColor() {
    return frameInactiveColor;
  }

  /**
   * Returns the highlight color of the food or 'apple' of the snake game.
   *
   * @return the food color
   */
  public Color getFoodColor() {
    return foodColor;
  }

  /**
   * Returns the background color of the game.
   *
   * @return the background color
   */
  public Color getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * Returns the indication if the color pattern is designed as dark theme. Depending on this value,
   * the NeuralNetwork parametrization may be colored differently.
   *
   * @return true, if this is a dark theme
   */
  public boolean isDarkTheme() {
    return darkTheme;
  }

  /**
   * As not all elements of the application are controlled by Java code directly, additional css
   * files are used additionally. This method returns the file name of the theme specific css file
   * to-be-used with the selected theme.
   *
   * @return the css file name of the selected theme
   */
  public String getCss() {
    return css;
  }

}
