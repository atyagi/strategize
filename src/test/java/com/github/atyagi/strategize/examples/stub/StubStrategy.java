package com.github.atyagi.strategize.examples.stub;

import com.github.atyagi.strategize.Strategy;

public abstract class StubStrategy implements Strategy<Void> {

    @Override
    public Void get() {
        return null;
    }
}
