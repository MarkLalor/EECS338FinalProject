package com.markyidi.eecs338.finalproject;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapReduce {
    public static <T, K, V> Map<K, V> mapReduce(final Collection<T> input, final MRMapper<T, K, V> mapper, final MRReducer<V> reducer) {
        final Collection<Collection<Map.Entry<K, V>>> mapped = input.parallelStream()
                .map(mapper::map)
                .collect(Collectors.toList());

        final Map<K, Collection<V>> shuffled = MapReduce.shuffle(mapped);

        return shuffled.entrySet().parallelStream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> reducer.reduce(entry.getValue())));
    }

    private static <K, V> void addToMultimap(Map<K, Collection<V>> multimap, Map.Entry<K, V> entry) {
        if (multimap.containsKey(entry.getKey())) {
            multimap.put(entry.getKey(), new LinkedList<>());
        }

        multimap.get(entry.getKey()).add(entry.getValue());
    }

    private static <K, V> Map<K, Collection<V>> shuffle(Collection<Collection<Map.Entry<K, V>>> mapped) {
        final Map<K, Collection<V>> shuffleMap = new ConcurrentHashMap<>();

        mapped.stream()
                .flatMap(Collection::stream)
                .forEach(entry -> addToMultimap(shuffleMap, entry));

        return shuffleMap;
    }
}
