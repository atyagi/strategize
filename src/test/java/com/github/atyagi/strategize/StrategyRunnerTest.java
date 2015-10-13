package com.github.atyagi.strategize;

import com.github.atyagi.strategize.examples.counting.CountingStrategy;
import com.github.atyagi.strategize.util.FunctionalHelpers;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.github.atyagi.strategize.domain.StrategyExecutionResult;
import com.github.atyagi.strategize.examples.counting.ForLoopCounter;
import com.github.atyagi.strategize.examples.counting.SizeMethodCounter;
import com.github.atyagi.strategize.examples.stub.StubStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class StrategyRunnerTest {

    private StrategyRunner runner;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector();
        this.runner = new StrategyRunner("com.github.atyagi.strategize", injector);
    }

    @Test
    public void findAndCreateAll_WillFindAllCountingStrategiesAndCreateThem() {
        List<Object> testList = Lists.newArrayList(1, 2, 3, 4, 5);
        List<CountingStrategy> strategies = runner.findAndCreateAll(
                CountingStrategy.class, s -> s.setList(testList));

        assertThat(strategies)
                .hasSize(2)
                .hasAtLeastOneElementOfType(ForLoopCounter.class)
                .hasAtLeastOneElementOfType(SizeMethodCounter.class)
                .hasOnlyElementsOfType(CountingStrategy.class);

        for (CountingStrategy strategy : strategies) {
            assertThat(strategy.getList())
                    .hasSize(5)
                    .isEqualTo(testList);
        }
    }

    @Test
    public void findAndCreateAll_WillReturnAnEmptyListIfNoStrategiesAreFound() {
        List<StubStrategy> strategies = runner.findAndCreateAll(StubStrategy.class, FunctionalHelpers.empty());
        assertThat(strategies).isEmpty();
    }

    @Test
    public void executeAll_ReturnsMapContainingTheCorrectKeyValuePairs() throws ExecutionException {
        List<Object> testList = Lists.newArrayList(1, 2, 3, 4, 5);
        List<CountingStrategy> strategies = runner.findAndCreateAll(
                CountingStrategy.class, s -> s.setList(testList));

        StrategyExecutionResult<Double> result = runner.executeAll(strategies);

        assertThat(result.getFutureStrategyResult(ForLoopCounter.class))
                .isInstanceOf(CompletableFuture.class)
                .isNotNull();

        assertThat(result.getFutureStrategyResult(SizeMethodCounter.class))
                .isInstanceOf(CompletableFuture.class)
                .isNotNull();

        List<Double> values = result.getAllCompletedValues();

        assertThat(values).hasSize(2)
                .contains((double) 5);
    }

    @Test
    public void executeAll_ReturnsAnEmptyMapIfEmptyListPassed() {
        List<StubStrategy> strategies = runner.findAndCreateAll(StubStrategy.class, FunctionalHelpers.empty());
        StrategyExecutionResult result = runner.executeAll(strategies);

        assertThat(result.getFutureResults()).isEmpty();
    }
}