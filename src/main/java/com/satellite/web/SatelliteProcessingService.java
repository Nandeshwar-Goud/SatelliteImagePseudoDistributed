package com.satellite.web;

import com.satellite.pseudo.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

@Service
public class SatelliteProcessingService {

    private volatile int total = 0;
    private volatile int processed = 0;
    private volatile String state = "IDLE";

    public void processDataset(File datasetDir, int tileSize) {

        new Thread(() -> {
            try {
                state = "RUNNING";

                File[] images = datasetDir.listFiles(
                        (d, n) -> n.endsWith(".jpg")
                                || n.endsWith(".png")
                                || n.endsWith(".jpeg"));

                if (images == null || images.length == 0) {
                    state = "ERROR";
                    return;
                }

                total = images.length;
                processed = 0;

                new File(Config.OUTPUT_DIR).mkdirs();

                ExecutorService pool = Executors.newFixedThreadPool(Config.MAX_THREADS);

                for (File imgFile : images) {

                    Mat image = ImageLoader.loadImage(
                            imgFile.getAbsolutePath());

                    List<Mat> tiles = ImageSplitter.split(image, tileSize);

                    List<Future<?>> futures = new ArrayList<>();

                    for (Mat tile : tiles) {
                        futures.add(pool.submit(
                                new TileTask(tile)));
                    }

                    for (Future<?> f : futures) {
                        f.get();
                    }

                    Mat result = ImageMerger.merge(image, tiles, tileSize);

                    Imgcodecs.imwrite(
                            Config.OUTPUT_DIR + "/processed_" + imgFile.getName(),
                            result);

                    processed++;
                }

                pool.shutdown();
                state = "COMPLETED";

            } catch (Exception e) {
                e.printStackTrace();
                state = "ERROR";
            }
        }).start();
    }

    public Map<String, Object> getStatus() {
        return Map.of(
                "state", state,
                "total", total,
                "processed", processed);
    }

    public String[] getResults() {
        File out = new File(Config.OUTPUT_DIR);
        return out.exists() ? out.list() : new String[0];
    }
}
