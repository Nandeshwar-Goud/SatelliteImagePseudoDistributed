package com.satellite.pseudo;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageLoader {
    public static Mat loadImage(String path) {
        Mat image = Imgcodecs.imread(path);
        if (image.empty()) {
            throw new RuntimeException("Failed to load image: " + path);
        }
        return image;
    }
}
