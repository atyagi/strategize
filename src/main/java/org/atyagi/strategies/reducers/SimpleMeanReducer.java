package org.atyagi.strategies.reducers;

import com.google.common.collect.Lists;
import org.atyagi.strategies.StrategyReducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.DoubleStream;

import static org.atyagi.strategies.util.Filters.*;

/**
 * Reduces a series of CompletableFutures as a
 * simple mean, meaning every value has
 */
public class SimpleMeanReducer implements StrategyReducer<Double> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Double reduce(Map<String, CompletableFuture<Double>> strategies) {
        Iterator<String> iterator = strategies.keySet().iterator();

        DoubleStream values = Lists.newArrayList(iterator).parallelStream().map(method -> {
            CompletableFuture<Double> future = strategies.get(method);
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("An error occurred when ");
                return null;
            }
        }).filter(notNull()).mapToDouble(t -> t);

        return values.average().orElse(0);
    }

}
