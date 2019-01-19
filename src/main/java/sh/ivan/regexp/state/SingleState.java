package sh.ivan.regexp.state;

import sh.ivan.regexp.Action;
import sh.ivan.regexp.symbol.Symbol;

import static sh.ivan.regexp.Action.CONSUME;
import static sh.ivan.regexp.Action.DIE;

public class SingleState extends State {
    public SingleState(Symbol symbol) {
        super(symbol);
    }

    @Override
    public Action accept(char c) {
        return symbol.matches(c) ? CONSUME : DIE;
    }
}
