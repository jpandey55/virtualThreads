package org.example.loom;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class HttpPlayer {

    private final static int NUM_THREADS = 500_000;

    public static void main(String[] args) {
        System.out.println("Thread Name 1:" + Thread.currentThread());
        ThreadFactory factory = Thread.ofVirtual().name("request-handler-",0).factory();

        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
            IntStream.range(0, NUM_THREADS).forEach(j -> {
//                System.out.println("Thread Name 2:" + Thread.currentThread());
                Future<String> submit = executor.submit(new UserRequestHandler());
//                    try {
//                        submit.get();
//                        System.out.println("[" + submit.get() + "]");
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    } catch (ExecutionException e) {
//                        throw new RuntimeException(e);
//                    }
//                System.out.println("Thread Name 6:" + Thread.currentThread());
            });
        }

        System.out.println("Ending Thread Name 2:" + Thread.currentThread());

    }
}
