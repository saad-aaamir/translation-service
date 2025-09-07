package com.application.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Map;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationExportResponse {
    private String locale;
    private Map<String, String> translations; // key -> content
    private List<String> tags;
    private long totalCount;
    private String exportedAt;
}