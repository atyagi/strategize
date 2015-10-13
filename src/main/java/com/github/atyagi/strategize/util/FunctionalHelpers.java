package com.github.atyagi.strategize.util;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A series of functional helpers for
 * syntactical sugar littered through the code
 */
public class FunctionalHelpers {

    /**
     * Predicate to make sure only non-null values
     * are allowed
     *
     * @param <T> Type of value to be checked
     * @return {@link Predicate} that checks non-null
     */
    public static <T> Predicate<T> notNull() {
        return o -> o != null;
    }

    /**
     * A consumer that simply returns, does nothing
     * else
     *
     * @param <T> Type of value passed in to the consumer
     * @return {@link Consumer} that does nothing
     */
    public static <T> Consumer<T> empty() {
        return o -> {
        };
    }
}
