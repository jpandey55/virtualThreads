package org.example.threads;

class SimpleRunnable implements Runnable {

    @Override
    public void run() {

        System.out.println("Starting Simple Thread");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        System.out.println("Ending Simple Thread");
    }
}