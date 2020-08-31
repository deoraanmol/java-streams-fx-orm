package exceptions;

public class NoLeaderException extends Exception {
    public NoLeaderException() {
        super("This Team does not have any member with Leader Type Personality");
    }
}
