package com.markyidi.eecs338.finalproject;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapReduce {
    public static <T, K, V> Collection<Map.Entry<K, V>> mapReduce(final Collection<T> input, final MRMapper<T, K, V> mapper, final MRReducer<V> reducer) {
        final Collection<Collection<Map.Entry<K, V>>> mapped = input.stream().map(mapper::map).collect(Collectors.toList());

        final Map<K, Collection<V>> shuffled = MapReduce.shuffle(mapped);


        return shuffled.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), reducer.reduce(entry.getValue())))
    }

    private static <K, V> Map<K, Collection<V>> shuffle(Collection<Collection<Map.Entry<K, V>>> mapped) {
        final Map<K, Collection<V>> shuffleMap = new ConcurrentHashMap<>();


        return shuffleMap;
    }
}
