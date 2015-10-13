package com.github.atyagi.strategize;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Injector;
import com.github.atyagi.strategize.domain.StrategyExecutionResult;
import org.reflections.Reflections;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * The StrategyRunner has two core methods that act as the
 * entry way to start handling data appropriately:
 *
 * {@link StrategyRunner#findAndCreateAll(Class, Consumer)} and
 * {@link StrategyRunner#executeAll(List)}
 *
 */
@Singleton
public class StrategyRunner {

    private final Reflections reflections;
    private final Injector injector;

    @Inject
    public StrategyRunner(String classpath, Injector injector) {
        this.reflections = new Reflections(classpath);
        this.injector = injector;
    }

    /**
     * Finds all strategies of a specific type and instantiates them, along
     * with setting the necessary data based on the dataSetter
     *
     * @param strategy   The strategy class reference for classpath searching
     * @param dataSetter A {@link Consumer} that is run after class construction for adding
     *                   any necessary data to the strategy, i.e. calling setters
     * @param <T>        The strategy class generic for the return value
     * @return A list of instantiated and ready strategies for execution
     */
    @SuppressWarnings("unchecked")
    public <T extends Strategy> List<T> findAndCreateAll(Class<T> strategy, Consumer<T> dataSetter) {
        Set<Class> classes = reflections.getSubTypesOf((Class) strategy);

        if (classes.isEmpty()) {
            return Lists.newArrayList();
        }

        List<T> list = (List<T>) Lists.newArrayList(classes.iterator())
                .parallelStream()
                .map(clazz -> (Strategy) injector.getInstance(clazz))
                .collect(toList());

        list.forEach(dataSetter::accept);

        return list;
    }

    /**
     * Throws all strategies into an execution pool to
     * be executed asynchronously in parallel
     *
     * @param strategies List of strategies
     * @param <T>        The strategy type
     * @param <R>        The return value type
     * @return A map of CompletableFuture that holds the return value
     * of each strategy execution keyed to the executing class
     */
    @SuppressWarnings("unchecked")
    public <T extends Strategy, R> StrategyExecutionResult<R> executeAll(List<T> strategies) {
        if (strategies.isEmpty()) {
            return new StrategyExecutionResult();
        }

        String strategyName = strategies.get(0).getClass().getName();
        ExecutorService executor = createExecutorServiceOfSize(strategies.size(), strategyName);

        Map<Class, CompletableFuture<R>> results = strategies.parallelStream()
                .collect(toMap(
                        Object::getClass,
                        t -> CompletableFuture.supplyAsync((Supplier) t, executor)
                ));

        return new StrategyExecutionResult(results, executor);
    }

    /**
     * Creates an ExecutorService for executing the tasks in parallel
     *
     * @param size     The thread pool size
     * @param strategy The abstract strategy name for naming
     * @return An {@link ExecutorService} to run the tasks
     */
    protected ExecutorService createExecutorServiceOfSize(int size, String strategy) {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setNameFormat(strategy + "-%d")
                .setDaemon(true)
                .build();

        return Executors.newFixedThreadPool(size, factory);
    }

}
