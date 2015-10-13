package com.github.atyagi.strategize;

import java.util.function.Supplier;

/**
 * This is the core Strategy interface that needs to be implemented to leverage
 * the library. Each Strategy represents a unit of work that will be called
 * by the resulting {@link Supplier}
 *
 * @param <T> The return type of the strategy's task
 */
public interface Strategy<T> extends Supplier<T> {

    /**
     * Used as the entry point for concurrent processing
     * based on the extended Supplier interface
     *
     * @return The strategy's return value
     */
    T get();
}
