package util;

public class State {

    private static State instance;
    private boolean interrupted;

    private State() {}

    public static State getInstance() {
        if (instance == null) {
            instance = new State();
        }
        return instance;
    }

    public void setInterrupt(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public boolean isInterrupted() {
        return interrupted;
    }
}
