package com.application.service;

import com.application.dto.response.TranslationExportResponse;

import java.util.Map;

public interface ExportService {
    TranslationExportResponse exportTranslationsByLocale(String locale);
    Map<String, String> exportTranslationsAsMap(String locale);
    TranslationExportResponse exportAllTranslations();
}
