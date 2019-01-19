package sh.ivan.regexp;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatcherTest {

    @Test
    public void testZeroOrOne() {
        Matcher matcher = new Matcher("abb?\\??a");
        assertThat(matcher.matches("aba")).isTrue();
        assertThat(matcher.matches("abba")).isTrue();
        assertThat(matcher.matches("abb?a")).isTrue();
        assertThat(matcher.matches("abbaa")).isFalse();
    }

    @Test
    public void testOneOrMore() {
        Matcher matcher = new Matcher("fo+baz+");
        assertThat(matcher.matches("foooooooooooooobaz")).isTrue();
        assertThat(matcher.matches("fobaz")).isTrue();
        assertThat(matcher.matches("fbaz")).isFalse();
        assertThat(matcher.matches("fooba")).isFalse();
        assertThat(matcher.matches("foobar")).isFalse();
    }

    @Test
    public void testCharacterClass() {
        Matcher matcher = new Matcher("[a-g]*");
        assertThat(matcher.matches("age")).isTrue();
        assertThat(matcher.matches("arg")).isFalse();
    }

}
