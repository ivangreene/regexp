package sh.ivan.regexp.state;

import sh.ivan.regexp.Action;
import sh.ivan.regexp.symbol.Symbol;

public abstract class State {
    protected final Symbol symbol;

    public State(Symbol symbol) {
        this.symbol = symbol;
    }

    public abstract Action accept(char c);

    public void reset() {
    }

    public boolean satisfied() {
        return false;
    }
}
