package sh.ivan.regexp.symbol;

import sh.ivan.regexp.state.State;

/**
 * Represents a symbol that can be quantified and matched against in a {@link State}
 */
public interface Symbol {
    boolean matches(char c);
}
