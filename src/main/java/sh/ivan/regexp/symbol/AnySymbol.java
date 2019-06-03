package sh.ivan.regexp.symbol;

/**
 * This Symbol will always match any character.
 */
public class AnySymbol implements Symbol {

    /**
     * The singleton instance of this Symbol.
     */
    public static final AnySymbol INSTANCE = new AnySymbol();

    @Override
    public boolean matches(char c) {
        return true;
    }

}
