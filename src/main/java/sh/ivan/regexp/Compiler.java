package sh.ivan.regexp;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;
import org.intellij.lang.annotations.Language;
import sh.ivan.regexp.state.State;
import sh.ivan.regexp.symbol.AnySymbol;
import sh.ivan.regexp.symbol.ClassSymbol;
import sh.ivan.regexp.symbol.LiteralSymbol;
import sh.ivan.regexp.symbol.Symbol;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;
import java.util.regex.PatternSyntaxException;

import static sh.ivan.regexp.state.StateSupplierFactory.boundedState;
import static sh.ivan.regexp.state.StateSupplierFactory.oneOrMoreState;
import static sh.ivan.regexp.state.StateSupplierFactory.singleState;
import static sh.ivan.regexp.state.StateSupplierFactory.zeroOrMoreState;
import static sh.ivan.regexp.state.StateSupplierFactory.zeroOrOneState;

public abstract class Compiler {

    public static List<Supplier<State>> parse(@Language("RegExp") String pattern) {
        List<Supplier<State>> states = new LinkedList<>();

        ListIterator<Character> iterator = Chars.asList(pattern.toCharArray()).listIterator();
        boolean isEscaped = false;
        Symbol last = null;
        char c;

        while (iterator.hasNext()) {
            c = iterator.next();
            if (isEscaped) {
                last = new LiteralSymbol(c);
                isEscaped = false;
                continue;
            }
            switch (c) {
                case '\\':
                    isEscaped = true;
                    break;
                case '?': case '+': case '*': case '{':
                    states.add(parseQuantifier(c, last, iterator, pattern));
                    last = null;
                    break;
                case '[':
                    last = parseClass(iterator);
                    break;
                case '.':
                    states.add(singleState(last));
                    last = AnySymbol.INSTANCE;
                    break;
                default:
                    if (last == null) {
                        last = new LiteralSymbol(c);
                        break;
                    }
                    states.add(singleState(last));
                    last = new LiteralSymbol(c);
            }
        }

        if (last != null) {
            states.add(singleState(last));
        }

        return Lists.reverse(states);
    }

    private static Supplier<State> parseQuantifier(char quant, Symbol symbol, ListIterator<Character> iterator, String pattern) {
        if (symbol == null) {
            throw new PatternSyntaxException("Unexpected quantifier `" + quant + "`", pattern, iterator.previousIndex());
        }
        switch (quant) {
            case '?':
                return zeroOrOneState(symbol);
            case '+':
                return oneOrMoreState(symbol);
            case '{':
                return parseRangeQuantifier(symbol, iterator, pattern);
            case '*':
            default:
                return zeroOrMoreState(symbol);
        }
    }

    private static Supplier<State> parseRangeQuantifier(Symbol symbol, ListIterator<Character> iterator, String pattern) {
        char n = iterator.next();
        StringBuilder lower = new StringBuilder();
        StringBuilder upper = new StringBuilder();
        StringBuilder which = lower;
        while (n != '}') {
            n = iterator.next();
            switch (n) {
                case ',':
                    if (lower.length() == 0) {
                        lower.append('0');
                    }
                    if (which != lower) {
                        throw new PatternSyntaxException("Unexpected extra `,` in range quantifier", pattern, iterator.previousIndex());
                    }
                    which = upper;
                    break;
                case '0': case '1': case '2': case '3':
                case '4': case '5': case '6': case '7':
                case '8': case '9':
                    which.append(n);
                    break;
                default:
                    throw new PatternSyntaxException("Unexpected `" + n + "` in range quantifier", pattern, iterator.previousIndex());
            }
        }
        if (upper.length() == 0) {
            return boundedState(symbol, Integer.parseInt(lower.toString()), Integer.MAX_VALUE);
        }
        return boundedState(symbol, Integer.parseInt(lower.toString()), Integer.parseInt(upper.toString()));
    }

    private static ClassSymbol parseClass(Iterator<Character> iterator) {
        ClassSymbol.Builder classSymbolBuilder = ClassSymbol.builder();
        Character last = null;
        Character current;
        boolean isRange = false;

        do {
            current = iterator.next();
            if (isRange) {
                classSymbolBuilder.addRange(last, current);
                isRange = false;
                continue;
            }
            switch (current) {
                case '\\':
                    classSymbolBuilder.add(iterator.next());
                    break;
                case '-':
                    isRange = true;
                    break;
                default:
                    if (last != null) {
                        classSymbolBuilder.add(last);
                    }
                    last = current;
            }
        } while (iterator.hasNext() && current != ']');

        return classSymbolBuilder.build();
    }

}
