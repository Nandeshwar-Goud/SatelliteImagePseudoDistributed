package com.satellite.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProcessingController {

    private final SatelliteProcessingService service;
    private File currentDatasetDir;

    public ProcessingController(SatelliteProcessingService service) {
        this.service = service;
    }

    // ================= UPLOAD DATASET =================
    @PostMapping("/upload-dataset")
    public ResponseEntity<?> uploadDataset(
            @RequestParam("files") MultipartFile[] files) {

        try {
            String root = System.getProperty("user.dir");
            currentDatasetDir = new File(root, "dataset/current");

            if (currentDatasetDir.exists()) {
                for (File f : currentDatasetDir.listFiles()) {
                    f.delete();
                }
            } else {
                currentDatasetDir.mkdirs();
            }

            int count = 0;

            for (MultipartFile file : files) {
                if (file.isEmpty())
                    continue;

                String cleanName = new File(file.getOriginalFilename()).getName();

                File dest = new File(currentDatasetDir, cleanName);
                file.transferTo(dest);
                count++;
            }

            if (count == 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "No valid images uploaded"));
            }

            return ResponseEntity.ok(Map.of("totalImages", count));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ================= START PROCESS =================
    @PostMapping("/start-processing")
    public ResponseEntity<?> startProcessing(
            @RequestBody(required = false) Map<String, Integer> body) {

        if (currentDatasetDir == null || !currentDatasetDir.exists()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No dataset uploaded"));
        }

        int tileSize = 200;
        if (body != null && body.containsKey("tileSize")) {
            int userSize = body.get("tileSize");
            if (userSize > 0)
                tileSize = userSize;
        }

        service.processDataset(currentDatasetDir, tileSize);
        return ResponseEntity.ok().build();
    }

    // ================= STATUS =================
    @GetMapping("/status")
    public Map<String, Object> status() {
        return service.getStatus();
    }

    // ================= RESULTS =================
    @GetMapping("/results")
    public String[] results() {
        return service.getResults();
    }
}
