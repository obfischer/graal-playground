package net.sweblog.playground.graal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;

public class TopTenBenchmark {

    private Stream<String> fileLines(String path) {
        try {
            return Files.lines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BenchmarkMode(Mode.SampleTime)
    @Benchmark
    public void topten(Blackhole blackhole) {
        Arrays.stream(new String[]{"large.txt"})
              .flatMap(this::fileLines)
              .flatMap(line -> Arrays.stream(line.split("\\b")))
              .map(word -> word.replaceAll("[^a-zA-Z]", ""))
              .filter(word -> word.length() > 0)
              .map(String::toLowerCase)
              .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
              .entrySet().stream()
              .sorted((a, b) -> -a.getValue().compareTo(b.getValue()))
              .limit(10)
              .forEach(e -> blackhole.consume(String.format("%s = %d%n", e.getKey(), e.getValue())));
    }
}
