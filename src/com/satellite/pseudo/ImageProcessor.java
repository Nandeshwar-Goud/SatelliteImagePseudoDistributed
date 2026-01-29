package com.satellite.pseudo;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {

    public static void process(Mat tile) {
        Mat gray = new Mat();
        Mat edges = new Mat();

        Imgproc.cvtColor(tile, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(gray, edges, 80, 160);

        Imgproc.cvtColor(edges, tile, Imgproc.COLOR_GRAY2BGR);
    }
}
