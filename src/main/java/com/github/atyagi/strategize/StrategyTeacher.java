package com.github.atyagi.strategize;

import com.github.atyagi.strategize.domain.StrategyExecutionStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class StrategyTeacher {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final StrategyRepository repository;

    @Inject
    public StrategyTeacher(StrategyRepository repository) {
        this.repository = repository;
    }

    public void markSuccessful(Class abstractGroup, Class successfulClass) {
        logger.info("Marking {} in group {} as successful", successfulClass.getSimpleName(), abstractGroup.getSimpleName());
        StrategyExecutionStats currentStats = repository.getExecutionStatsForGroup(abstractGroup);
        currentStats.addNewExecution();
        currentStats.markStrategyAsSuccessful(successfulClass);
        repository.updateExecutionStats(currentStats);
    }

}
