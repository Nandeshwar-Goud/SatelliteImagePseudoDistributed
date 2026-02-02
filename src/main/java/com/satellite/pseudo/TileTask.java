package com.satellite.pseudo;

import org.opencv.core.Mat;

public class TileTask implements Runnable {

    private final Mat tile;

    public TileTask(Mat tile) {
        this.tile = tile;
    }

    @Override
    public void run() {
        ImageProcessor.process(tile);
    }
}
