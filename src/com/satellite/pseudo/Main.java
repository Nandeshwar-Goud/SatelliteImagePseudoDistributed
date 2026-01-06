package com.satellite.pseudo;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        Mat image = ImageLoader.loadImage(Config.INPUT_IMAGE);

        List<Mat> tiles = ImageSplitter.split(image, Config.TILE_SIZE);
        List<Worker> workers = new ArrayList<>();

        int id = 1;
        for (Mat tile : tiles) {
            Worker w = new Worker(tile, id++);
            workers.add(w);
            w.start();
        }

        for (Worker w : workers) {
            try {
                w.join();
            } catch (InterruptedException ignored) {
            }
        }

        Mat result = ImageMerger.merge(image, tiles, Config.TILE_SIZE);
        Imgcodecs.imwrite(Config.OUTPUT_IMAGE, result);

        System.out.println("Final output generated successfully");
    }
}
