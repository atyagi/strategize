package com.github.atyagi.strategize.reducers;

import com.github.atyagi.strategize.StrategyReducer;
import com.github.atyagi.strategize.StrategyRepository;
import com.github.atyagi.strategize.domain.StrategyExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class WeightedMeanReducer implements StrategyReducer<Double> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final StrategyRepository repository;

    @Inject
    public WeightedMeanReducer(StrategyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Double reduce(StrategyExecutionResult<Double> result) {
        List<Class> strategies = result.getAllExecutedClasses();
        return null;
    }
}
