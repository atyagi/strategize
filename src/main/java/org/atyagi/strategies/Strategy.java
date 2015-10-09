package org.atyagi.strategies;

import java.util.function.Supplier;

public interface Strategy extends Supplier {

    /**
     * Used as the entry point for async processing
     * based on the extended Supplier interface
     * @return The strategy's return value
     */
    Object get();
}
