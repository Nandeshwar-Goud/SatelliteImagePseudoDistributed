package com.satellite.pseudo;

public class DelaySimulator {

    public static void simulate(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
