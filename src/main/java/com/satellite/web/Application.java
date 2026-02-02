package com.satellite.web;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    static {
        // Load OpenCV native library once at startup
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.load(
                new java.io.File("native/libopencv_java4140.dylib").getAbsolutePath());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Satellite Image Processing Backend Started");
    }
}
