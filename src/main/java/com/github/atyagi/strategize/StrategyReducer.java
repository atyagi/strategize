package com.github.atyagi.strategize;

import com.github.atyagi.strategize.domain.StrategyExecutionResult;

/**
 * Every grouping of strategies requires logic around determining the
 * value to use for the final result. This interface enforces a common
 * way to do that
 *
 * @param <R> The type of the return value
 */
public interface StrategyReducer<R> {

    /**
     * Given the values of each strategy's execution,
     * determine how to reduce each value down to the final
     * using custom heuristics
     *
     * @param result The {@link StrategyExecutionResult} that has the execution results
     * @return The specific value that will act as the overarching strategy's final return
     */
    R reduce(StrategyExecutionResult<R> result);


}
