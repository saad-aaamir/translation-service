package com.application.service;

import com.application.dto.request.TranslationCreateRequest;
import com.application.dto.request.TranslationSearchRequest;
import com.application.dto.request.TranslationUpdateRequest;
import com.application.dto.response.TranslationResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface TranslationService {
    TranslationResponse createTranslation(TranslationCreateRequest request);
    TranslationResponse updateTranslation(Long id, TranslationUpdateRequest request);
    TranslationResponse getTranslationById(Long id);
    TranslationResponse getTranslationByKeyAndLocale(String key, String locale);
    Page<TranslationResponse> searchTranslations(TranslationSearchRequest request);
    List<TranslationResponse> getTranslationsByLocale(String locale);
    List<TranslationResponse> getTranslationsByTag(String tagName);
    void deleteTranslation(Long id);
    void deleteTranslationsByLocale(String locale);
    long countTranslationsByLocale(String locale);
    List<TranslationResponse> fullTextSearch(String searchTerm);
}