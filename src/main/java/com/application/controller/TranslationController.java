package com.application.controller;

import com.application.dto.request.TranslationCreateRequest;
import com.application.dto.request.TranslationSearchRequest;
import com.application.dto.request.TranslationUpdateRequest;
import com.application.dto.response.ApiResponse;
import com.application.dto.response.TranslationResponse;
import com.application.service.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
@Tag(name = "Translation Management", description = "APIs for managing translations")
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping
    @Operation(summary = "Create a new translation")
    public ResponseEntity<ApiResponse<TranslationResponse>> createTranslation(
            @Valid @RequestBody TranslationCreateRequest request) {
        TranslationResponse translationResponse = translationService.createTranslation(request);
        ApiResponse<TranslationResponse> response = ApiResponse.<TranslationResponse>builder()
                .success(true)
                .message("Translation created successfully")
                .data(translationResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing translation")
    public ResponseEntity<ApiResponse<TranslationResponse>> updateTranslation(
            @PathVariable Long id,
            @Valid @RequestBody TranslationUpdateRequest request) {
        TranslationResponse translationResponse = translationService.updateTranslation(id, request);
        ApiResponse<TranslationResponse> response = ApiResponse.<TranslationResponse>builder()
                .success(true)
                .message("Translation updated successfully")
                .data(translationResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get translation by ID")
    public ResponseEntity<ApiResponse<TranslationResponse>> getTranslationById(@PathVariable Long id) {
        TranslationResponse translationResponse = translationService.getTranslationById(id);
        ApiResponse<TranslationResponse> response = ApiResponse.<TranslationResponse>builder()
                .success(true)
                .message("Translation retrieved successfully")
                .data(translationResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/key/{key}/locale/{locale}")
    @Operation(summary = "Get translation by key and locale")
    public ResponseEntity<ApiResponse<TranslationResponse>> getTranslationByKeyAndLocale(
            @PathVariable String key,
            @PathVariable String locale) {
        TranslationResponse translationResponse = translationService.getTranslationByKeyAndLocale(key, locale);
        ApiResponse<TranslationResponse> response = ApiResponse.<TranslationResponse>builder()
                .success(true)
                .message("Translation retrieved successfully")
                .data(translationResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    @Operation(summary = "Search translations with criteria")
    public ResponseEntity<ApiResponse<Page<TranslationResponse>>> searchTranslations(
            @Valid @RequestBody TranslationSearchRequest request) {
        Page<TranslationResponse> translations = translationService.searchTranslations(request);
        ApiResponse<Page<TranslationResponse>> response = ApiResponse.<Page<TranslationResponse>>builder()
                .success(true)
                .message("Translation search completed successfully")
                .data(translations)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/locale/{locale}")
    @Operation(summary = "Get all translations for a specific locale")
    public ResponseEntity<ApiResponse<List<TranslationResponse>>> getTranslationsByLocale(
            @PathVariable String locale) {
        List<TranslationResponse> translations = translationService.getTranslationsByLocale(locale);
        ApiResponse<List<TranslationResponse>> response = ApiResponse.<List<TranslationResponse>>builder()
                .success(true)
                .message("Translations retrieved successfully")
                .data(translations)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tag/{tagName}")
    @Operation(summary = "Get all translations for a specific tag")
    public ResponseEntity<ApiResponse<List<TranslationResponse>>> getTranslationsByTag(
            @PathVariable String tagName) {
        List<TranslationResponse> translations = translationService.getTranslationsByTag(tagName);
        ApiResponse<List<TranslationResponse>> response = ApiResponse.<List<TranslationResponse>>builder()
                .success(true)
                .message("Translations retrieved successfully")
                .data(translations)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/fulltext")
    @Operation(summary = "Full-text search across translations")
    public ResponseEntity<ApiResponse<List<TranslationResponse>>> fullTextSearch(
            @Parameter(description = "Search term") @RequestParam String searchTerm) {
        List<TranslationResponse> translations = translationService.fullTextSearch(searchTerm);
        ApiResponse<List<TranslationResponse>> response = ApiResponse.<List<TranslationResponse>>builder()
                .success(true)
                .message("Full-text search completed successfully")
                .data(translations)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/locale/{locale}")
    @Operation(summary = "Count translations by locale")
    public ResponseEntity<ApiResponse<Long>> countTranslationsByLocale(@PathVariable String locale) {
        long count = translationService.countTranslationsByLocale(locale);
        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .success(true)
                .message("Translation count retrieved successfully")
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a translation")
    public ResponseEntity<ApiResponse<Void>> deleteTranslation(@PathVariable Long id) {
        translationService.deleteTranslation(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Translation deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/locale/{locale}")
    @Operation(summary = "Delete all translations for a specific locale")
    public ResponseEntity<ApiResponse<Void>> deleteTranslationsByLocale(@PathVariable String locale) {
        translationService.deleteTranslationsByLocale(locale);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Translations deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}