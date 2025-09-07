package com.application.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationResponse {
    private Long id;
    private String translationKey;
    private String content;
    private String locale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<TagResponse> tags;
}