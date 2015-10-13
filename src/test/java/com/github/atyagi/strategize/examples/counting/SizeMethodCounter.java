package com.github.atyagi.strategize.examples.counting;

import java.util.List;

public class SizeMethodCounter extends CountingStrategy {
    @Override
    public Double count(List<Object> list) {
        return (double) list.size();
    }
}
