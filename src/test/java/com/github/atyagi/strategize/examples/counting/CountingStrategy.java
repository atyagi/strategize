package com.github.atyagi.strategize.examples.counting;

import com.github.atyagi.strategize.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class CountingStrategy implements Strategy<Double> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Object> list;

    public abstract Double count(List<Object> list);

    @Override
    public Double get() {
        logger.info("Executing {} strategy for counting", getClass().getName());
        return count(list);
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public List<Object> getList() {
        return list;
    }
}
