package com.markyidi.eecs338.finalproject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCount {
    public static void main(String[] args) {
        String dataDirectory = args[0];
        File ddFile = new File(dataDirectory);
        List<Path> files = Arrays.stream(ddFile.listFiles())
                .filter(File::isFile)
                .map(File::toPath)
                .collect(Collectors.toList());

        Collection<Map.Entry<String, Integer>> results = MapReduce.mapReduce(files, WordCount::docWordCount, WordCount::reduce);
    }

    public static Collection<Map.Entry<String, Integer>> docWordCount(Path item) {
        try (Stream<String> s = Files.lines(item)
                .flatMap(Pattern.compile("\\s+")::splitAsStream)) {
            return s
                    .map(x -> new AbstractMap.SimpleEntry<>(x, 1))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Shoot.");
        }
    }

    public static Integer reduce(Collection<Integer> shuffled) {
        return shuffled.stream().mapToInt(Integer::intValue).sum();
    }
}
