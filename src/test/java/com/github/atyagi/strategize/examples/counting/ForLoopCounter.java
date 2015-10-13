package com.github.atyagi.strategize.examples.counting;

import java.util.List;

public class ForLoopCounter extends CountingStrategy {

    @Override
    public Double count(List<Object> list) {
        Double count = (double) 0;
        for (Object value : list) {
            logger.info("Adding to count");
            count++;
        }
        return count;
    }
}
