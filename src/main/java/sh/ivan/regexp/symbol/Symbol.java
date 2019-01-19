package sh.ivan.regexp.symbol;

import sh.ivan.regexp.state.State;

/**
 * Represents a symbol that can be quantified and matched against in a {@link State}
 */
public abstract class Symbol {
    public abstract boolean matches(char c);
}
