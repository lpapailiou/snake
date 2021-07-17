package main.agent;

import ai.netadapter.GameDecorator;
import javafx.animation.Timeline;
import main.Config;
import main.State;

public abstract class Agent {

    Config config = Config.getInstance();
    Timeline timeline;
    int speed;
    State state;
    GameDecorator decorator;

    public void build() {
        if (state == null) {
            throw new IllegalStateException("incomplete build! state ist not set!");
        }
    }

    public Agent setState(State state) {
        this.state = state;
        return this;
    }

    public Agent setSpeed(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("speed must not be below 0!");
        }
        this.speed = speed;
        return this;
    }

    void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}
