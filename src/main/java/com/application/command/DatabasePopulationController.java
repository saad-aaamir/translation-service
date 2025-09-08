package com.application.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/admin/population")
@RequiredArgsConstructor
@Slf4j
public class DatabasePopulationController {

    private final DatabasePopulationService populationService;
    @PostMapping("/populate")
    public ResponseEntity<Map<String, Object>> populateDatabase(
            @RequestParam(defaultValue = "100000") int count) {

        log.info("Received request to populate database with {} records", count);

        try {
            long startTime = System.currentTimeMillis();
            populationService.populateDatabase(count);
            long endTime = System.currentTimeMillis();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Database populated successfully",
                    "recordsCreated", count,
                    "executionTimeMs", endTime - startTime
            ));

        } catch (Exception e) {
            log.error("Error populating database", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Failed to populate database: " + e.getMessage()
            ));
        }
    }
}