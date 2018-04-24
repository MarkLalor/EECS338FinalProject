package com.markyidi.eecs338.finalproject;

import java.util.Collection;

@FunctionalInterface
public interface MRReducer<V> {
    V reduce(Collection<V> shuffled);
}
