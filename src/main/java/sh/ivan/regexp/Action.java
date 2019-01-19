package sh.ivan.regexp;

public enum Action {
    /**
     * Indicates the matcher should consume the character and continue
     */
    CONSUME,
    /**
     * Indicates the matcher should continue without consuming the character
     */
    CONTINUE,
    /**
     * Indicates the character was not matched and this is unacceptable
     */
    DIE,
    /**
     * Indicates the matcher should attempt matching the next character against this State
     */
    TRY_NEXT,
}
