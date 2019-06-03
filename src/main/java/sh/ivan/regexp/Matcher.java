package sh.ivan.regexp;

import com.google.common.collect.Streams;
import org.intellij.lang.annotations.Language;
import sh.ivan.regexp.state.State;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Matcher {
    private List<Supplier<State>> states;

    public Matcher(@Language("RegExp") String pattern) {
        this.states = Compiler.parse(pattern);
    }

    public boolean matches(String string) {
        try {
            ListIterator<State> stateListIterator = states.stream()
                    .map(Supplier::get)
                    .collect(Collectors.toList())
                    .listIterator();
            for (char c : string.toCharArray()) {
                examine(stateListIterator, c);
            }
            return Streams.stream(stateListIterator)
                    .allMatch(State::satisfied);
        } catch (NoMatchException e) {
            return false;
        }
    }

    private void examine(ListIterator<State> stateStack, char c) throws NoMatchException {
        try {
            switch (stateStack.next().accept(c)) {
                case CONSUME:
                    break;
                case CONTINUE:
                    examine(stateStack, c);
                    break;
                case TRY_NEXT:
                    stateStack.previous();
                    break;
                case DIE:
                    throw new NoMatchException();
            }
        } catch (NoSuchElementException e) {
            throw new NoMatchException();
        }
    }

    private static class NoMatchException extends Exception {
    }

}
