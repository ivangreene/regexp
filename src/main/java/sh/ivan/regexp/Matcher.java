package sh.ivan.regexp;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;
import sh.ivan.regexp.state.OneOrMoreState;
import sh.ivan.regexp.state.SingleState;
import sh.ivan.regexp.state.State;
import sh.ivan.regexp.state.ZeroOrMoreState;
import sh.ivan.regexp.state.ZeroOrOneState;
import sh.ivan.regexp.symbol.ClassSymbol;
import sh.ivan.regexp.symbol.LiteralSymbol;
import sh.ivan.regexp.symbol.Symbol;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Matcher {
    private final String pattern;
    private List<State> states;

    public Matcher(String pattern) {
        this.pattern = pattern;
        parse();
    }

    private void parse() {
        List<State> states = new LinkedList<>();
        boolean isQuantifier = false;
        boolean isEscaped = false;
        Symbol last = null;
        char c;
        Iterator<Character> iterator = Chars.asList(this.pattern.toCharArray())
                .iterator();
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
                case '?':
                    if (last == null) {
                        throw new PatternParseException("Quantifier `?` found without a symbol to match against");
                    }
                    isQuantifier = true;
                    states.add(new ZeroOrOneState(last));
                    last = null;
                    break;
                case '+':
                    if (last == null) {
                        throw new PatternParseException("Quantifier `+` found without a symbol to match against");
                    }
                    isQuantifier = true;
                    states.add(new OneOrMoreState(last));
                    last = null;
                    break;
                case '*':
                    if (last == null) {
                        throw new PatternParseException("Quantifier `*` found without a symbol to match against");
                    }
                    isQuantifier = true;
                    states.add(new ZeroOrMoreState(last));
                    last = null;
                    break;
                case '[':
                    last = parseClass(iterator);
                    break;
                default:
                    isQuantifier = false;
                    if (last == null) {
                        last = new LiteralSymbol(c);
                        break;
                    }
                    states.add(new SingleState(last));
                    last = new LiteralSymbol(c);
            }
        }
        if (!isQuantifier) {
            states.add(new SingleState(last));
        }
        this.states = Lists.reverse(states);
    }

    private ClassSymbol parseClass(Iterator<Character> iterator) {
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

    public boolean matches(String string) {
        try {
            String current = null;
            String last = null;
            Stack<State> stateStack = new Stack<>();
            stateStack.addAll(states);
            stateStack.forEach(State::reset);
            for (char c : string.toCharArray()) {
                examine(stateStack, c);
            }
            return stateStack.stream()
                    .allMatch(State::satisfied);
        } catch (NoMatchException e) {
            return false;
        }
    }

    private void examine(Stack<State> stateStack, char c) throws NoMatchException {
        try {
            switch (stateStack.peek().accept(c)) {
                case CONSUME:
                    stateStack.pop();
                    break;
                case CONTINUE:
                    stateStack.pop();
                    examine(stateStack, c);
                    break;
                case TRY_NEXT:
                    break;
                case DIE:
                    throw new NoMatchException();
            }
        } catch (EmptyStackException e) {
            throw new NoMatchException();
        }
    }

    private static class NoMatchException extends Exception {
    }

}
