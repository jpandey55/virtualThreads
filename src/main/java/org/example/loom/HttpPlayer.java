package org.example.loom;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class HttpPlayer {

    private final static int NUM_THREADS = 1;

    public static void main(String[] args) {
        ThreadFactory factory = Thread.ofVirtual().name("request-handler-",0).factory();

        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
            IntStream.range(0, NUM_THREADS).forEach(j -> {
                executor.submit(new UserRequestHandler());
            });
        }


    }
}
