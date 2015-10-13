package com.github.atyagi.strategize;


import com.github.atyagi.strategize.domain.StrategyExecutionStats;

/**
 * A repository to get data for Strategy class weighting
 */
public interface StrategyRepository {

    /**
     * This returns the execution stats of a particular grouping,
     * including all of the specific success rates for each
     * value.
     *
     * @param group The class grouping of executed strategy
     * @return The {@link StrategyExecutionStats} of
     */
    StrategyExecutionStats getExecutionStatsForGroup(Class group);

}
