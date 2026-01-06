package com.satellite.pseudo;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {

    public static void process(Mat tile) {
        Imgproc.cvtColor(tile, tile, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(tile, tile, 80, 160);
    }
}
