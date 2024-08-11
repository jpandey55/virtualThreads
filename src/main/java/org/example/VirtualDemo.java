package org.example;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class VirtualDemo {
    public static void main(String[] args) {

        System.out.println("Starting");
        System.out.println(Thread.currentThread().getName());

        /*
        two ways to create virtual threads:
        1. Use builder(not thread safe) to create virtual threads
        2. Use factory(thread safe) to create virtual threads
         */

        var vbuider = Thread.ofVirtual().name("vt", 0);

        Thread t1 = vbuider.start( () -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = vbuider.start( () -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            System.out.println("Exception");
        }


        // Create a Thread factory
        ThreadFactory factory
                = Thread.ofVirtual().name("userthread", 0).factory();
        Thread t3 = factory.newThread( () -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t3.start();

        try {
            t3.join();
        } catch (Exception e) {
            System.out.println("Exception");
        }

        System.out.println("Ending");
        System.out.println(Thread.currentThread().getName());

    }
}
