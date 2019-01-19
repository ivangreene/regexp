package sh.ivan.regexp.state;

import sh.ivan.regexp.Action;
import sh.ivan.regexp.symbol.Symbol;

import static sh.ivan.regexp.Action.CONTINUE;
import static sh.ivan.regexp.Action.DIE;
import static sh.ivan.regexp.Action.TRY_NEXT;

public class OneOrMoreState extends State {
    protected boolean matched = false;

    public OneOrMoreState(Symbol symbol) {
        super(symbol);
    }

    @Override
    public Action accept(char c) {
        if (symbol.matches(c)) {
            this.matched = true;
            return TRY_NEXT;
        } else {
            return matched ? CONTINUE : DIE;
        }
    }

    @Override
    public void reset() {
        this.matched = false;
    }

    @Override
    public boolean satisfied() {
        return this.matched;
    }
}
