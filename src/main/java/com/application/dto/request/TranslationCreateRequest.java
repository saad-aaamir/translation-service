package com.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationCreateRequest {
    @NotBlank(message = "Translation key is required")
    @Size(max = 255, message = "Translation key must not exceed 255 characters")
    private String translationKey;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Locale is required")
    @Size(max = 10, message = "Locale must not exceed 10 characters")
    private String locale;

    private Set<String> tagNames;
}