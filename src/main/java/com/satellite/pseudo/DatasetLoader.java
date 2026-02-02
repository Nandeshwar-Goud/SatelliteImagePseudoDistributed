package com.satellite.pseudo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatasetLoader {
    public static List<File> loadImages(String folderPath) {

        File folder = new File(folderPath);
        File[] files = folder
                .listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg"));

        List<File> images = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                images.add(f);
            }
        }
        return images;
    }
}
