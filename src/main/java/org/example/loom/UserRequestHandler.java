package org.example.loom;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.*;
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
            System.out.println("Thread Name db:" + Thread.currentThread());
            return networkCaller.makeCall(5);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String restCall() {
        NetworkCaller networkCaller = new NetworkCaller("rest");
        try {
            System.out.println("Thread Name rest:" + Thread.currentThread());
            return networkCaller.makeCall(10);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String externalCall() {
        NetworkCaller networkCaller = new NetworkCaller("extr");
        try {
            System.out.println("Thread Name rest:" + Thread.currentThread());
            return networkCaller.makeCall(10);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String dbCall1() {
        NetworkCaller networkCaller = new NetworkCaller("dbcall1");
        try {
            return networkCaller.makeCall(2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String dbCall2() {
        NetworkCaller networkCaller = new NetworkCaller("dbCall2");
        try {
            return networkCaller.makeCall(3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String restCall1() {
        NetworkCaller networkCaller = new NetworkCaller("restCall1");
        try {
            return networkCaller.makeCall(4);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String restCall2() {
        NetworkCaller networkCaller = new NetworkCaller("restCall2");
        try {
            return networkCaller.makeCall(5);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String call() throws Exception {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            //db1 and db2 sequentially
            // rest1 and rest2 parallel
            String result = CompletableFuture.supplyAsync(this::dbCall1, executorService)
                    .thenApply(s -> s + "," + this.dbCall2())
                    .thenCompose(s -> {
                        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(this::restCall1, executorService);
                        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(this::restCall2, executorService);
                        return future1.thenCombine(
                                future2, (result1, result2) -> {
                                    try {
                                        return s + "," + future1.get() + "," + future2.get();
                                    } catch (InterruptedException | ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }).join();
            return result;
        }

        //ALSO

        // Sequential coding ..
//		String result1 = dbCall1();
//		String result2 = dbCall2();
//
//		// complicated parallel threads code in limited to the block below
//		try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
//
//			String result = CompletableFuture
//								.supplyAsync(this::restCall1, service)
//								.thenCombine(
//										 CompletableFuture.supplyAsync(this::restCall2, service)
//										,this::mergeResults)
//								.join(); // join blocks in a virtual thread. so its okay.
//
//			String output = mergeResults(result1, result2, result);
//
//			System.out.println(output);
//			return output;
//
//    }}

//    private String mergeResults(String result1, String result2, String result) {
//        return result1 +"," + result2 + "," + result;
//    }

    }
    private String getString() {
        System.out.println("Thread Name 3:" + Thread.currentThread());

        long startTime = System.nanoTime();
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
//           USING FUTURE:
//            Future<String> s1 = executorService.submit(this::dbCall);
//            Future<String> s2 = executorService.submit(this::restCall);
//
//            String result = String.format("[%s %s]", s1.get(), s2.get());
//

//            String result = executorService.invokeAll(Arrays.asList(this::dbCall, this::restCall))
//                    .stream()
//                    .map(objectFuture -> {
//                        try {
//                            System.out.println("Thread Name 4:" + Thread.currentThread());
//                            return (String) objectFuture.get();
//                        } catch (Exception e) {
//                            return null;
//                        }
//                    })
//                    .collect(Collectors.joining(","));

            //USING Completable Future


            String result = CompletableFuture
                    .supplyAsync(this::dbCall, executorService)
                    .thenCombine(
                            CompletableFuture.supplyAsync(this::restCall, executorService)
                                    ,(result1, result2) -> {
                                return result1 +","+ result2;
                            })
                    .thenApply(s -> {
                        //both DB and Rest calls are completed
                        String r = externalCall();
                        return s + "," + r;
                    })
                    .join();

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.println("Elapsed time in milliseconds: " + duration / 1_000_000);
//            System.out.println(result);
            System.out.println("Thread Name 5:" + Thread.currentThread());

            return result;
        }
    }
}
