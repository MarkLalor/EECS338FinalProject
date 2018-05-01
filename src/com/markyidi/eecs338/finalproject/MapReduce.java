package com.markyidi.eecs338.finalproject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapReduce {
    /**
     * Uses the MapReduce algorithm to transform an {@link Collection} of input items into output data using the
     * provided map and reduce functions, specified by passing in an {@link MRMapper} and {@link MRReducer}.
     * @param input A {@link Collection} of input-typed values that will be mapped by the provided map function.
     * @param mapper A function that takes one of the input items and produces a set of key-value entries.
     *               See {@link MRMapper} for more details.
     * @param reducer A function that takes a Collection of values (which were produced by the mapper) and reduces
     *                them to a value of the same type. See {@link MRReducer} for more details.
     * @param <I> The input type, which is manipulated by the {@link MRMapper}.
     * @param <K> The key type, which is used to map together items by the shuffle operation.
     * @param <V> The output type, which is stored along with the key type.
     * @return A {@link Map} of key types to output types after performing the map, shuffle, and reduce operations.
     */
    public static <I, K, V> Map<K, V> mapReduce(final Collection<I> input, final MRMapper<I, K, V> mapper, final MRReducer<V> reducer) {
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
        if (!multimap.containsKey(entry.getKey())) {
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
