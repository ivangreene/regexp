package sh.ivan.regexp.state;

import sh.ivan.regexp.Action;
import sh.ivan.regexp.symbol.Symbol;

import static sh.ivan.regexp.Action.CONSUME;
import static sh.ivan.regexp.Action.CONTINUE;
import static sh.ivan.regexp.Action.DIE;
import static sh.ivan.regexp.Action.TRY_NEXT;

/**
 * A State defined by upper and lower bounds that must be met.
 */
public class BoundedState extends State {

    private final int lower;
    private final int upper;

    private int matched = 0;

    public BoundedState(Symbol symbol, int lower, int upper) {
        super(symbol);
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public Action accept(char c) {
        if (symbol.matches(c)) {
            matched++;
            if (matched >= upper) {
                return CONSUME;
            }
            return TRY_NEXT;
        } else {
            if (satisfied()) {
                return CONTINUE;
            }
            return DIE;
        }
    }

    @Override
    public boolean satisfied() {
        return matched >= lower;
    }
}
