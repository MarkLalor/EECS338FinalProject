package com.markyidi.eecs338.finalproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class WordCount {
    public static Pattern tokenPattern = Pattern.compile("\\b\\w*[A-z,-][A-z,-]+\\w*\\b");

    public static void main(String[] args) {
        String dataDirectory = args[0];
        String outputFn = args[1];
        File ddFile = new File(dataDirectory);
        List<Path> files = Arrays.stream(ddFile.listFiles())
                .filter(File::isFile)
                .map(File::toPath)
                .collect(Collectors.toList());

        Map<String, Integer> results = MapReduce.mapReduce(files, WordCount::docWordCount, WordCount::reduce);
        writeResults(results, outputFn);
    }

    public static Collection<Map.Entry<String, Integer>> docWordCount(Path item) {
        List<Map.Entry<String, Integer>> counts = new LinkedList<>();
        try {
            String s = new String(Files.readAllBytes(item), Charset.forName("UTF-8"));
            Matcher m = tokenPattern.matcher(s);
            while (m.find()) {
                counts.add(new AbstractMap.SimpleEntry<>(m.toMatchResult().group().toLowerCase(), 1));
            }
            return counts;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Shoot.");
        }
    }

    public static Integer reduce(Collection<Integer> shuffled) {
        return shuffled.stream().mapToInt(Integer::intValue).sum();
    }

    public static void writeResults(Map<String, Integer> results, String outputFn) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(results.size());
        sortedList.addAll(results.entrySet());
        sortedList.sort(Comparator.comparing(entry -> -entry.getValue()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFn))) {
            for (Map.Entry<String, Integer> entry : sortedList) {
                writer.write(String.format("%s,%d\n", entry.getKey(), entry.getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Darn.");
        }
    }
}
