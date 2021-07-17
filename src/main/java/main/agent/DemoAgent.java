package main.agent;

import ai.netadapter.GameDecorator;
import ai.netadapter.Serializer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class DemoAgent extends Agent {

    @Override
    public void build() {
        super.build();
        decorator = new GameDecorator(Serializer.load());
        state.setGame(decorator.getGame());
        timeline = new Timeline(new KeyFrame(Duration.millis(speed), event -> {

            decorator.perform();
            if (!decorator.isActive()) {
                stopTimer();
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        state.setTimeline(timeline);
        timeline.play();
    }

}
