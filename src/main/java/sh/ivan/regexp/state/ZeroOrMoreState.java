package sh.ivan.regexp.state;

import sh.ivan.regexp.symbol.Symbol;

public class ZeroOrMoreState extends OneOrMoreState {
    public ZeroOrMoreState(Symbol symbol) {
        super(symbol);
        this.matched = true;
    }

    @Override
    public void reset() {
    }
}
