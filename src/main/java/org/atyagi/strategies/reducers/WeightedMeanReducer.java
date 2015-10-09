package org.atyagi.strategies.reducers;

import org.atyagi.strategies.StrategyReducer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WeightedMeanReducer implements StrategyReducer<Double> {
    @Override
    public Double reduce(Map<String, CompletableFuture<Double>> strategies) {
        return null;
    }
}
