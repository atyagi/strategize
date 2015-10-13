## Strategize

*Because there are too many ways to solve one problem*

[![Build Status](https://travis-ci.org/atyagi/strategize.svg?branch=master)](https://travis-ci.org/atyagi/strategize)
 
## Purpose

With any data processing there's a need to find certain outputs given some input, 
but over time the core logic to find those outputs can change. Sometimes,
certain scenarios require different logic to be ran based on situational elements.
At the end of the day, the different logic can be boiled down to a `Strategy` on how
to find some arbitrary output given some input.

What this library aims to do is provide a framework to allow for multiple Strategies to be 
run in parallel, each operating independently of each other, and then reducing the outputs
down to a specific value based on extensible logic.

## Requirements

* Java 8
* Google Guice

## Usage

### Simple Example

```java

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.atyagi.strategize.examples.counting.CountingStrategy;
import org.atyagi.strategize.StrategyRunner;
import org.atyagi.strategize.domain.StrategyExecutionResult;

import static org.atyagi.strategize.util.FunctionalHelpers.empty;
import static org.atyagi.strategize.util.FunctionalHelpers.notNull;

public class Example {
    public static void main(String[] args) {
        // Create a Guice Injector instance
        Injector injector = Guice.createInjector();
        
        // Create the Strategy Runner to find and execute strategies
        StrategyRunner runner = new StrategyRunner("my.classpath.of.strategies", injector);
        
        // Find all strategies in provided classpath and instantiate each one
        List<CountingStrategy> countingStrategies = runner.findAndCreateAll(CountingStrategy.class, empty());
        
        // Execute all strategies provided
        StrategyExecutionResult<Double> result = runner.executeAll(countingStrategies);
        
        // Create a reducer and reduce all the values down to a single value 
        SimpleMeanReducer reducer = new SimpleMeanReducer();
        Double count = reducer.reduce(result);
        
        System.out.println("The count is " + count);
    }
}
```

So what exactly happened here? We created a `StrategyRunner` that went out and found all of the strategies of a type, 
along with a consumer function to set any necessary data in each `Strategy`. Once we have all of the strategies
instantiated, we then had the runner execute all of them and create a result object. At this point,
we can pass the result into a reducer to use certain heuristics to select the best value 
and return a single value to be used.


