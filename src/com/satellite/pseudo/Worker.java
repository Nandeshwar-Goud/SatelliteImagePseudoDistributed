package com.satellite.pseudo;

import org.opencv.core.Mat;

public class Worker extends Thread {

    private final Mat tile;
    private final int id;

    public Worker(Mat tile, int id) {
        this.tile = tile;
        this.id = id;
    }

    @Override
    public void run() {
        DelaySimulator.simulate(Config.WORKER_DELAY_MS);
        ImageProcessor.process(tile);
        System.out.println("Worker " + id + " finished");
    }
}
