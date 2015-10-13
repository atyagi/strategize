package com.github.atyagi.strategize.domain;

import com.google.common.collect.Maps;
import com.github.atyagi.strategize.examples.counting.ForLoopCounter;
import com.github.atyagi.strategize.examples.counting.SizeMethodCounter;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StrategyExecutionResultTest {

    @Test
    public void getFutureStrategyResult_ReturnsTheCompletableFuture() throws Exception {
        StrategyExecutionResult<Double> result = getDefaultTestResult();
        CompletableFuture<Double> future = result.getFutureStrategyResult(ForLoopCounter.class);
        assertThat(future.get()).isEqualTo(3);
    }

    @Test
    public void getFutureStrategyResult_ReturnsNullWhenClassDoesNotExist() {
        StrategyExecutionResult<Double> result = getDefaultTestResult();
        CompletableFuture<Double> future = result.getFutureStrategyResult(SizeMethodCounter.class);
        assertThat(future).isNull();
    }

    @Test
    public void getStrategyResult_ReturnsTheCompletedValue() throws Exception {
        StrategyExecutionResult<Double> result = getDefaultTestResult();
        Double value = result.getStrategyResult(ForLoopCounter.class);
        assertThat(value).isEqualTo(3);
    }

    @Test
    public void getStrategyResult_ReturnsNullIfTheClassDoesNotExistInMap() throws Exception {
        StrategyExecutionResult<Double> result = getDefaultTestResult();
        Double value = result.getStrategyResult(SizeMethodCounter.class);
        assertThat(value).isNull();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ExecutionException.class)
    public void getStrategyResult_ThrowsExceptionIfFutureDoesNotResolve() throws Exception {
        CompletableFuture mock = mock(CompletableFuture.class);
        when(mock.get()).thenThrow(ExecutionException.class);
        Map<Class, CompletableFuture<Double>> map = getStubMap(ForLoopCounter.class, (double) 3);
        map.put(SizeMethodCounter.class, mock);

        StrategyExecutionResult result = new StrategyExecutionResult(map);

        result.getStrategyResult(SizeMethodCounter.class);
    }

    @Test
    public void getAllFutureValues_ReturnsOnlyTheValuesAsFuture() throws Exception {
        StrategyExecutionResult<Double> result = getDefaultTestResult();
        List<CompletableFuture<Double>> values = result.getAllFutureValues();

        assertThat(values).hasSize(1);
        CompletableFuture<Double> value = values.get(0);
        assertThat(value.get()).isEqualTo(3);
    }

    @Test
    public void getAllFutureValues_ReturnsEmptyListWhenNoValues() throws Exception {
        StrategyExecutionResult result = new StrategyExecutionResult();
        List values = result.getAllFutureValues();
        assertThat(values).isEmpty();
    }

    @Test
    public void getAllCompletedResults_ReturnsMapOfCompletedResults() throws Exception {
        StrategyExecutionResult<Double> result = getDefaultTestResult();
        Map<Class, Double> map = result.getAllCompletedResults();
        assertThat(map).hasSize(1)
                .containsOnlyKeys(ForLoopCounter.class)
                .containsValue((double) 3);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ExecutionException.class)
    public void getAllCompletedResults_ThrowsErrorWhenFutureDoesNotResolve() throws Exception {
        CompletableFuture mock = mock(CompletableFuture.class);
        when(mock.get()).thenThrow(ExecutionException.class);
        Map<Class, CompletableFuture<Double>> map = getStubMap(ForLoopCounter.class, (double) 3);
        map.put(SizeMethodCounter.class, mock);
        StrategyExecutionResult result = new StrategyExecutionResult(map);

        result.getAllCompletedResults();
    }

    private Map<Class, CompletableFuture<Double>> getStubMap(Class clazz, Double value) {
        Map<Class, CompletableFuture<Double>> map = Maps.newHashMap();
        map.put(clazz, CompletableFuture.completedFuture(value));
        return map;
    }

    private StrategyExecutionResult<Double> getDefaultTestResult() {
        Map<Class, CompletableFuture<Double>> map = getStubMap(ForLoopCounter.class, (double) 3);
        return new StrategyExecutionResult<>(map);
    }
}