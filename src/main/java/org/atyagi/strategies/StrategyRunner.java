package org.atyagi.strategies;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Injector;
import org.reflections.Reflections;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.stream.Collectors.*;

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
     * with setting the necessary data based on the consumer
     *
     * @param strategy The strategy class reference for classpath searching
     * @param consumer The consumer that is run after class construction for adding
     *                 any necessary data to the strategy
     * @param <T> The strategy class generic for the return value
     * @return A list of instantiated and ready strategies for execution
     */
    @SuppressWarnings("unchecked")
    public <T extends Strategy> List<T> findAndCreateAll(Class<T> strategy, Consumer<T> consumer) {
        Set<Class> classes = reflections.getSubTypesOf((Class) strategy);

        if(classes.isEmpty()) {
            return Lists.newArrayList();
        }

        //instantiates the objects using Guice
        List<T> list = (List<T>) Lists.newArrayList(classes.iterator())
                .parallelStream()
                .map(clazz -> (Strategy) injector.getInstance(clazz))
                .collect(toList());

        //sets the objects with data based on the consumer
        list.forEach(consumer::accept);

        return list;
    }

    /**
     * Throws all strategies into an execution pool to
     * be executed asynchronously in parallel
     *
     * @param strategies List of strategies
     * @param <T> The strategy type
     * @param <R> The return value type
     * @return A list of CompletableFuture that holds the return value
     *  of each strategy execution
     */
    @SuppressWarnings("unchecked")
    public <T extends Strategy, R> Map<String, CompletableFuture<R>> executeAll(List<T> strategies) {
        if (strategies.isEmpty()) {
            return Maps.newConcurrentMap();
        }

        String strategyName = strategies.get(0).getClass().getName();
        ExecutorService executor = createExecutorServiceOfSize(strategies.size(), strategyName);

        return strategies.parallelStream()
                .collect(toMap(
                        s -> s.getClass().getName(),
                        t -> CompletableFuture.supplyAsync((Supplier) t, executor)
                    ));
    }

    /**
     * Creates an ExecutorService
     * @param size
     * @param strategy
     * @return
     */
    private ExecutorService createExecutorServiceOfSize(int size, String strategy) {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setNameFormat(strategy + "-%d")
                .setDaemon(true)
                .build();

        return Executors.newFixedThreadPool(size, factory);
    }

}
