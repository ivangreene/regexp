package sh.ivan.regexp.symbol;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClassSymbol extends Symbol {
    private final Set<Character> values;

    public ClassSymbol(Character[] values) {
        this.values = ImmutableSet.copyOf(values);
    }

    public ClassSymbol(Collection<Character> values) {
        this.values = ImmutableSet.copyOf(values);
    }

    @Override
    public boolean matches(char c) {
        return values.contains(c);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Set<Character> values = new HashSet<>();

        private Builder() {
        }

        public Builder add(char value) {
            this.values.add(value);
            return this;
        }

        public Builder addRange(char first, char last) {
            for (char c = first; c <= last; c++) {
                add(c);
            }
            return this;
        }

        public ClassSymbol build() {
            return new ClassSymbol(values);
        }
    }

}
