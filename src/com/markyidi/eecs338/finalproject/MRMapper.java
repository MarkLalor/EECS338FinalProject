package com.markyidi.eecs338.finalproject;

import java.util.Collection;
import java.util.Map;

/**
 * A map function transforms a single input instance into a set of intermediate keys and values
 * @param <T> Input data type
 * @param <K> Intermediate key type
 * @param <V> Intermediate value type
 */
@FunctionalInterface
public interface MRMapper<T, K, V> {
    Collection<Map.Entry<K, V>> map(T item);
}
