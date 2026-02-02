package com.satellite.pseudo;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageLoader {

    public static Mat loadImage(String path) {
        Mat img = Imgcodecs.imread(path);
        if (img.empty()) {
            throw new RuntimeException("Failed to load image: " + path);
        }
        return img;
    }
}
