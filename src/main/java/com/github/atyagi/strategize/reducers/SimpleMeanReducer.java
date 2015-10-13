package com.github.atyagi.strategize.reducers;

import com.github.atyagi.strategize.StrategyReducer;
import com.github.atyagi.strategize.domain.StrategyExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Reduces a series of CompletableFutures as a
 * simple mean, meaning every value has the exact same
 * weight and each value gets averaged equally
 */
public class SimpleMeanReducer implements StrategyReducer<Double> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Double reduce(StrategyExecutionResult<Double> result) {
        try {
            List<Double> values = result.getAllCompletedValues();
            return values.parallelStream().mapToDouble(v -> v).average().orElse(0);
        } catch (ExecutionException e) {
            logger.error("An error occurred getting all the values", e);
            return (double) 0;
        }
    }

}
