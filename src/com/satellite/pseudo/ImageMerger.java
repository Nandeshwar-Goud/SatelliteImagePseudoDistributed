package com.satellite.pseudo;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.List;

public class ImageMerger {

    public static Mat merge(Mat original, List<Mat> tiles, int tileSize) {
        Mat result = original.clone();
        int index = 0;

        for (int y = 0; y < original.rows(); y += tileSize) {
            for (int x = 0; x < original.cols(); x += tileSize) {
                int w = Math.min(tileSize, original.cols() - x);
                int h = Math.min(tileSize, original.rows() - y);
                tiles.get(index++).copyTo(result.submat(new Rect(x, y, w, h)));
            }
        }
        return result;
    }
}
