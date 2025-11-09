package com.nanotrader.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LatencyStats {
    private final List<Long> samples = new ArrayList<>();

    public void record(long latencyNanos) {
        samples.add(latencyNanos);
    }

    public void printSummary() {
        if (samples.isEmpty()) {
            System.out.println("No samples recorded");
            return;
        }

        Collections.sort(samples);
        int size = samples.size();
        long sum = samples.stream().mapToLong(Long::longValue).sum();
        double mean = sum / (double) size;
        long p99 = samples.get((int) (size * 0.99));
        long max = samples.get(size - 1);

        System.out.printf("Messages: %,d | Mean: %.2f µs | 99th: %.2f µs | Max: %.2f µs%n",
                size, mean / 1000.0, p99 / 1000.0, max / 1000.0);
    }
}
