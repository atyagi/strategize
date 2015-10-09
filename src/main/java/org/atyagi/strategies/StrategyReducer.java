package org.atyagi.strategies;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Every grouping of strategies requires logic around determining the
 * value to use for the final result. This interface enforces a common
 * way to do that
 * @param <R> The type of the return value
 */
public interface StrategyReducer<R> {

    /**
     * Given the values of each strategy's execution,
     * determine how to reduce each value down to one
     * using custom heuristics
     * @param strategies The strategies that were executed and the calling class
     * @return The specific
     */
    R reduce(Map<String, CompletableFuture<R>> strategies);



}
