package com.markyidi.eecs338.finalproject;

import java.util.Collection;

/**
 * A reducer takes all the values mapped to an intermediate key and produces an final output value for the key
 * @param <V> Type of value
 */
@FunctionalInterface
public interface MRReducer<V> {
    V reduce(Collection<V> shuffled);
}
