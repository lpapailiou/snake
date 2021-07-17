package main.agent;

import game.Game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ManualAgent extends Agent {

    @Override
    public void build() {
        super.build();
        Game game = new Game();
        state.setGame(game);

        timeline = new Timeline(new KeyFrame(Duration.millis(speed), event -> {
            boolean isActive = game.moveSnake(state.getDirection());
            if (!isActive) {
                stopTimer();
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        state.setTimeline(timeline);
        timeline.play();
    }

}
