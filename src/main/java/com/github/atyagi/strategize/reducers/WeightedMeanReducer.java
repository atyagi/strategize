package com.github.atyagi.strategize.reducers;

import com.github.atyagi.strategize.StrategyReducer;
import com.github.atyagi.strategize.StrategyRepository;
import com.github.atyagi.strategize.domain.StrategyExecutionResult;
import com.github.atyagi.strategize.domain.StrategyExecutionStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class WeightedMeanReducer implements StrategyReducer<Double> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final StrategyRepository repository;
    private Class strategyGroup;

    @Inject
    public WeightedMeanReducer(StrategyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Double reduce(StrategyExecutionResult<Double> result) {
        List<Class> strategies = result.getAllExecutedClasses();
        StrategyExecutionStats stats = repository.getExecutionStatsForGroup(strategyGroup);

        /*
            (weight1*result1) + (weight2*result2) + ...
            ----------------------------------------------
             weight1 + weight2 + ...
         */

        return null;
    }

    public void setStrategyGroup(Class strategyGroup) {
        this.strategyGroup = strategyGroup;
    }
}
