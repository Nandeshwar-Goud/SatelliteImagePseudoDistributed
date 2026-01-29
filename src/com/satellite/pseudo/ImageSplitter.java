package com.satellite.pseudo;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

public class ImageSplitter {

    public static List<Mat> split(Mat image, int tileSize) {
        List<Mat> tiles = new ArrayList<>();

        for (int y = 0; y < image.rows(); y += tileSize) {
            for (int x = 0; x < image.cols(); x += tileSize) {
                int w = Math.min(tileSize, image.cols() - x);
                int h = Math.min(tileSize, image.rows() - y);
                Mat tile = new Mat(image, new Rect(x, y, w, h)).clone();
                tiles.add(tile);
            }
        }
        return tiles;
    }
}
