package com.satellite.pseudo;

public class Config {

    // Default values (used only if user doesn't provide input)
    public static final int DEFAULT_TILE_SIZE = 200;

    // Output directory for processed images
    public static final String OUTPUT_DIR = "output";

    // Threading / performance knobs
    public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    // Image formats allowed
    public static final String[] SUPPORTED_EXTENSIONS = {
            ".jpg", ".jpeg", ".png"
    };

    private Config() {
        // prevent instantiation
    }
}
