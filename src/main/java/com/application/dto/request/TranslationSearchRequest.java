package com.application.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationSearchRequest {
    private String locale;
    private String translationKey;
    private String content;
    private String tagName;
    private int page = 0;
    private int size = 20;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}