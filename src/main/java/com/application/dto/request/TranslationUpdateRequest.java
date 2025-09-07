package com.application.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationUpdateRequest {
    @NotBlank(message = "Content is required")
    private String content;

    private Set<String> tagNames;
}