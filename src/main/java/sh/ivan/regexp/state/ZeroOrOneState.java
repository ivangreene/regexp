package sh.ivan.regexp.state;

import sh.ivan.regexp.Action;
import sh.ivan.regexp.symbol.Symbol;

import static sh.ivan.regexp.Action.CONSUME;
import static sh.ivan.regexp.Action.CONTINUE;

public class ZeroOrOneState extends State {
    public ZeroOrOneState(Symbol symbol) {
        super(symbol);
    }

    @Override
    public Action accept(char c) {
        return symbol.matches(c) ? CONSUME : CONTINUE;
    }

    @Override
    public boolean satisfied() {
        return true;
    }
}
