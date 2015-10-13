package com.github.atyagi.strategize.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * The result of executing a series of strategies, which
 * maintains the raw results and
 *
 * @param <R> The return type of each strategy execution
 */
public class StrategyExecutionResult<R> {

    private final Map<Class, CompletableFuture<R>> classLevelResults;
    private final ExecutorService executorService;

    /**
     * Main constructor that takes all elements of the Result
     *
     * @param classLevelResults The map of the class/result key pairs
     * @param executorService   The ExecutorService that is running the strategies
     */
    public StrategyExecutionResult(Map<Class, CompletableFuture<R>> classLevelResults, ExecutorService executorService) {
        this.classLevelResults = classLevelResults;
        this.executorService = executorService;
    }

    /**
     * Takes in only result map, sets a null
     * ExecutorService
     *
     * @param classLevelResults The map of the class/result key pairs
     */
    public StrategyExecutionResult(Map<Class, CompletableFuture<R>> classLevelResults) {
        this.classLevelResults = classLevelResults;
        this.executorService = null;
    }

    /**
     * Empty constructor that sets a default HashMap and a
     * null ExecutorService
     */
    public StrategyExecutionResult() {
        this.classLevelResults = Maps.newHashMap();
        this.executorService = null;
    }

    /**
     * If key is not set, null is returned
     *
     * @param clazz The strategy class
     * @return The {@link CompletableFuture} container of the strategy's return value
     */
    public CompletableFuture<R> getFutureStrategyResult(Class clazz) {
        return classLevelResults.get(clazz);
    }

    /**
     * @param clazz The strategy class
     * @return The completed result of the strategy
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public R getStrategyResult(Class clazz) throws ExecutionException, InterruptedException {
        CompletableFuture<R> value = getFutureStrategyResult(clazz);
        if (value != null) {
            return value.get();
        }
        return null;
    }

    /**
     * @return All of the {@link CompletableFuture} container objects
     * without the strategy class reference
     */
    public List<CompletableFuture<R>> getAllFutureValues() {
        return Lists.newArrayList(classLevelResults.values());
    }

    /**
     * @return All of the values from each strategy execution
     * without the strategy class reference
     */
    public List<R> getAllCompletedValues() throws ExecutionException {
        return Lists.newArrayList(getAllCompletedResults().values());
    }

    /**
     * @return The raw results map container, but with
     * all {@link CompletableFuture} values resolved
     */
    public Map<Class, R> getAllCompletedResults() throws ExecutionException {
        Map<Class, R> completedResults = Maps.newConcurrentMap();
        List<Exception> errors = Lists.newArrayList();

        classLevelResults.forEach((clazz, future) -> {
            try {
                completedResults.put(clazz, future.get());
            } catch (InterruptedException | ExecutionException e) {
                errors.add(e);
            }
        });

        if (!errors.isEmpty()) {
            throw new ExecutionException(errors.get(0));
        }

        return completedResults;
    }

    /**
     * @return A list of all the classes that have executed a strategy
     */
    public List<Class> getAllExecutedClasses() {
        return Lists.newArrayList(classLevelResults.keySet());
    }

    /**
     * @return The raw results map container, all {@link CompletableFuture}
     * need to be resolved
     */
    public Map<Class, CompletableFuture<R>> getFutureResults() {
        return classLevelResults;
    }

    /**
     * @return The {@link ExecutorService} that ran the
     * strategies
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }
}
