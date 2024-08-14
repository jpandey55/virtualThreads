package org.example.threads;


public class ExampleLambda {
    
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting Main Thread ..");

        Thread.ofPlatform().start(() -> {
            
            System.out.println("Starting Simple Thread");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }

            System.out.println("Ending Simple Thread");
        });
        
        System.out.println("Ending Main Thread ..");

    }

}

