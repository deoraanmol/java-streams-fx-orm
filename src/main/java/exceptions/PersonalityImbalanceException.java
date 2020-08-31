package exceptions;

public class PersonalityImbalanceException extends Exception {
    public PersonalityImbalanceException() {
        super("Team has Personality Imbalance, it must have at least 3 different personalities");
    }
}
