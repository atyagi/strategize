package org.atyagi.strategies;


import java.util.Optional;

/**
 * Stores strategy weights in MongoDB for game to game and
 * week to week persistence for learning accuracies
 */
public interface StrategyRepository {

    Optional<StrategyWeight> findPlayTypeWeight(Class abstractClass);
}
