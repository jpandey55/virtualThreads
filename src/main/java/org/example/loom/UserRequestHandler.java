package org.example.loom;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserRequestHandler implements Callable<String> {


    public String sequentialCall() throws Exception {

        String s1 = dbCall();
        String s2 = restCall();

        String result = String.format("[%s %s]", s1, s2);
        System.out.println(result);
        return result;
    }

    private String dbCall() {
        NetworkCaller networkCaller = new NetworkCaller("data");
        try {
            return networkCaller.makeCall(2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String restCall() {
        NetworkCaller networkCaller = new NetworkCaller("rest");
        try {
            return networkCaller.makeCall(5);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String call() throws Exception {

        long startTime = System.nanoTime();
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
//            Future<String> s1 = executorService.submit(this::dbCall);
//            Future<String> s2 = executorService.submit(this::restCall);
//
//            String result = String.format("[%s %s]", s1.get(), s2.get());

            String result = executorService.invokeAll(Arrays.asList(this::dbCall, this::restCall))
                    .stream()
                    .map(objectFuture -> {
                        try {
                            return (String) objectFuture.get();
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .collect(Collectors.joining(","));

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.println("Elapsed time in milliseconds: " + duration / 1_000_000);
            System.out.println(result);
            return result;
        }
    }
}
