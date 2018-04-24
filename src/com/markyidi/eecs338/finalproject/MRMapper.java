package com.markyidi.eecs338.finalproject;

import java.util.Collection;
import java.util.Map;

@FunctionalInterface
public interface MRMapper<T, K, V> {
    Collection<Map.Entry<K, V>> map(T item);
}
