package sh.ivan.regexp.symbol;

public class LiteralSymbol extends Symbol {
    private final char value;

    public LiteralSymbol(char value) {
        this.value = value;
    }

    @Override
    public boolean matches(char c) {
        return this.value == c;
    }
}
