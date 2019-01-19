package sh.ivan.regexp;

public class PatternParseException extends RuntimeException {
    public PatternParseException() {
    }

    public PatternParseException(String message) {
        super(message);
    }
}
