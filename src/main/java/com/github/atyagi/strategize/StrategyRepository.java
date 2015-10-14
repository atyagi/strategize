package com.github.atyagi.strategize;


import com.github.atyagi.strategize.domain.StrategyExecutionStats;

/**
 * A repository to interact with an independent
 * persistence store for maintaining execution stats
 */
public interface StrategyRepository {

    /**
     * This returns the execution stats of a particular grouping,
     * including all of the specific success rates for each
     * value.
     *
     * @param group The class grouping of executed strategy
     * @return The {@link StrategyExecutionStats} of a particular
     * grouping
     */
    StrategyExecutionStats getExecutionStatsForGroup(Class group);

    /**
     * This is a generic update of all the stats of a particular
     * grouping.
     * <p>
     * This should act as a full merge of the parameter to what
     * is maintained in persistence
     *
     * @param stats The {@link StrategyExecutionStats} object that will be replaced
     */
    void updateExecutionStats(StrategyExecutionStats stats);

}
