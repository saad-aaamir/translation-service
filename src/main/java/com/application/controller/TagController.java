package com.application.controller;

import com.application.dto.request.TagCreateRequest;
import com.application.dto.response.ApiResponse;
import com.application.dto.response.TagResponse;
import com.application.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tag Management", description = "APIs for managing translation tags")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @Operation(summary = "Create a new tag")
    public ResponseEntity<ApiResponse<TagResponse>> createTag(@Valid @RequestBody TagCreateRequest request) {
        TagResponse tagResponse = tagService.createTag(request);
        ApiResponse<TagResponse> response = ApiResponse.<TagResponse>builder()
                .success(true)
                .message("Tag created successfully")
                .data(tagResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing tag")
    public ResponseEntity<ApiResponse<TagResponse>> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagCreateRequest request) {
        TagResponse tagResponse = tagService.updateTag(id, request);
        ApiResponse<TagResponse> response = ApiResponse.<TagResponse>builder()
                .success(true)
                .message("Tag updated successfully")
                .data(tagResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID")
    public ResponseEntity<ApiResponse<TagResponse>> getTagById(@PathVariable Long id) {
        TagResponse tagResponse = tagService.getTagById(id);
        ApiResponse<TagResponse> response = ApiResponse.<TagResponse>builder()
                .success(true)
                .message("Tag retrieved successfully")
                .data(tagResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get tag by name")
    public ResponseEntity<ApiResponse<TagResponse>> getTagByName(@PathVariable String name) {
        TagResponse tagResponse = tagService.getTagByName(name);
        ApiResponse<TagResponse> response = ApiResponse.<TagResponse>builder()
                .success(true)
                .message("Tag retrieved successfully")
                .data(tagResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all tags")
    public ResponseEntity<ApiResponse<List<TagResponse>>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        ApiResponse<List<TagResponse>> response = ApiResponse.<List<TagResponse>>builder()
                .success(true)
                .message("Tags retrieved successfully")
                .data(tags)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get all tags with pagination")
    public ResponseEntity<ApiResponse<Page<TagResponse>>> getAllTagsPaginated(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<TagResponse> tags = tagService.getAllTags(pageable);
        ApiResponse<Page<TagResponse>> response = ApiResponse.<Page<TagResponse>>builder()
                .success(true)
                .message("Tags retrieved successfully")
                .data(tags)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search tags by name")
    public ResponseEntity<ApiResponse<List<TagResponse>>> searchTagsByName(
            @Parameter(description = "Name to search for") @RequestParam String name) {
        List<TagResponse> tags = tagService.searchTagsByName(name);
        ApiResponse<List<TagResponse>> response = ApiResponse.<List<TagResponse>>builder()
                .success(true)
                .message("Tags search completed successfully")
                .data(tags)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/most-used")
    @Operation(summary = "Get most used tags")
    public ResponseEntity<ApiResponse<List<TagResponse>>> getMostUsedTags(
            @Parameter(description = "Number of tags to return") @RequestParam(defaultValue = "10") int limit) {
        List<TagResponse> tags = tagService.getMostUsedTags(limit);
        ApiResponse<List<TagResponse>> response = ApiResponse.<List<TagResponse>>builder()
                .success(true)
                .message("Most used tags retrieved successfully")
                .data(tags)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists")
    @Operation(summary = "Check if tag exists by name")
    public ResponseEntity<ApiResponse<Boolean>> existsByName(@RequestParam String name) {
        boolean exists = tagService.existsByName(name);
        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message("Tag existence check completed")
                .data(exists)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Tag deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}