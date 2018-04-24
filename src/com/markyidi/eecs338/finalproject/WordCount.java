package com.markyidi.eecs338.finalproject;

import java.util.Collection;

public class WordCount {
    public static void main(String[] args) {

    }

    public static Integer reduce(Collection<Integer> shuffled) {
        return shuffled.stream().mapToInt(Integer::intValue).sum();
    }
}
