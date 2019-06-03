package sh.ivan.regexp.state;

import sh.ivan.regexp.symbol.Symbol;

import java.util.function.Supplier;

/**
 * Factory methods to obtain {@link Supplier}s of {@link State}s.
 * The Suppliers themselves are threadsafe and calling {@link Supplier#get}
 * will provide either an immutable State or a new instance of a mutable State.
 */
public abstract class StateSupplierFactory {

    public static Supplier<State> boundedState(Symbol symbol, int lower, int upper) {
        return () -> new BoundedState(symbol, lower, upper);
    }

    public static Supplier<State> oneOrMoreState(Symbol symbol) {
        return boundedState(symbol, 1, Integer.MAX_VALUE);
    }

    public static Supplier<State> zeroOrMoreState(Symbol symbol) {
        return boundedState(symbol, 0, Integer.MAX_VALUE);
    }

    public static Supplier<State> zeroOrOneState(Symbol symbol) {
        return boundedState(symbol, 0, 1);
    }

    public static Supplier<State> singleState(Symbol symbol) {
        State state = new SingleState(symbol);
        return () -> state;
    }

}
