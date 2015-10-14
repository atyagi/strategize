package com.github.atyagi.strategize.domain;

import java.util.Map;

public class StrategyExecutionStats {

    /**
     * The grouping of the strategy
     * for this particular record
     */
    private Class strategyGroup;

    /**
     * The total number of executions this
     * particular grouping has had
     */
    private Long totalExecutions;

    /**
     * A map of each class and the number
     * of successes the particular class has had
     */
    private Map<Class, Long> executionStats;

    /**
     * Finds the success rate of a specific
     * Execution Strategy
     *
     * @param clazz The strategy class
     * @return The success rate
     */
    public Double getSuccessRateOfClass(Class clazz) {
        Long successCount = executionStats.get(clazz);
        return (double) (successCount / totalExecutions);
    }

    /**
     * Returns the absolute number of successful
     * executions for a strategy
     *
     * @param clazz The strategy class
     * @return The success count
     */
    public Long getSuccessCountOfClass(Class clazz) {
        return executionStats.get(clazz);
    }

    public void addNewExecution() {
        this.totalExecutions++;
    }

    public void markStrategyAsSuccessful(Class strategy) {
        Long count = executionStats.get(strategy);
        executionStats.put(strategy, count + 1);
    }

    public Class getStrategyGroup() {
        return strategyGroup;
    }

    public void setStrategyGroup(Class strategyGroup) {
        this.strategyGroup = strategyGroup;
    }

    public Long getTotalExecutions() {
        return totalExecutions;
    }

    public void setTotalExecutions(Long totalExecutions) {
        this.totalExecutions = totalExecutions;
    }

    public Map<Class, Long> getExecutionStats() {
        return executionStats;
    }

    public void setExecutionStats(Map<Class, Long> executionStats) {
        this.executionStats = executionStats;
    }
}
