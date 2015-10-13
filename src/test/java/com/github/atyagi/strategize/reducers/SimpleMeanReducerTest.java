package com.github.atyagi.strategize.reducers;

import com.google.common.collect.Maps;
import com.github.atyagi.strategize.domain.StrategyExecutionResult;
import com.github.atyagi.strategize.examples.counting.ForLoopCounter;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class SimpleMeanReducerTest {

    private SimpleMeanReducer reducer;

    @Before
    public void setUp() {
        this.reducer = new SimpleMeanReducer();
    }

    @Test
    public void reduce_takesTheCorrectAverageOnDoubles() {
        StrategyExecutionResult<Double> result = getClassCompletableFutureMap();
        Double value = reducer.reduce(result);
        assertThat(value).isEqualTo(10);
    }

    @Test
    public void reduce_returnsZeroIfNoStrategiesExist() {
        Double value = reducer.reduce(new StrategyExecutionResult<>());
        assertThat(value).isZero();
    }

    @Test
    public void reduce_catchesExceptionsAndReturnsZeroOnCompletionFailure() throws ExecutionException {
        StrategyExecutionResult mock = mock(StrategyExecutionResult.class);
        when(mock.getAllCompletedValues()).thenThrow(ExecutionException.class);

        Double value = reducer.reduce(mock);

        assertThat(value).isZero();
    }

    private StrategyExecutionResult getClassCompletableFutureMap() {
        Map<Class, CompletableFuture<Double>> map = Maps.newHashMap();
        map.put(ForLoopCounter.class, CompletableFuture.completedFuture((double) 5));
        map.put(ForLoopCounter.class, CompletableFuture.completedFuture((double) 10));
        return new StrategyExecutionResult(map, null);
    }
}