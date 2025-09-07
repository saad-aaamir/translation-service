package com.application.service.impl;

import com.application.dto.response.TranslationExportResponse;
import com.application.entity.Tag;
import com.application.entity.Translation;
import com.application.repository.TranslationRepository;
import com.application.service.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportServiceImpl implements ExportService {

    private final TranslationRepository translationRepository;

    @Override
    @Cacheable(value = "exportsByLocale", key = "#locale")
    @Transactional(readOnly = true)
    public TranslationExportResponse exportTranslationsByLocale(String locale) {
        log.debug("Exporting translations for locale: {}", locale);

        List<Translation> translations = translationRepository.findAllByLocaleWithTags(locale);

        Map<String, String> translationsMap = translations.stream()
                .collect(Collectors.toMap(
                        Translation::getTranslationKey,
                        Translation::getContent
                ));

        List<String> allTags = translations.stream()
                .flatMap(t -> t.getTags().stream())
                .map(Tag::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return TranslationExportResponse.builder()
                .locale(locale)
                .translations(translationsMap)
                .tags(allTags)
                .totalCount(translations.size())
                .exportedAt(LocalDateTime.now().toString())
                .build();
    }

    @Override
    @Cacheable(value = "translationsMapByLocale", key = "#locale")
    @Transactional(readOnly = true)
    public Map<String, String> exportTranslationsAsMap(String locale) {
        log.debug("Exporting translations as map for locale: {}", locale);

        List<Translation> translations = translationRepository.findByLocale(locale);
        return translations.stream()
                .collect(Collectors.toMap(
                        Translation::getTranslationKey,
                        Translation::getContent
                ));
    }

    @Override
    @Cacheable(value = "allTranslationsExport")
    @Transactional(readOnly = true)
    public TranslationExportResponse exportAllTranslations() {
        log.debug("Exporting all translations");

        List<Translation> translations = translationRepository.findAll();

        Map<String, String> translationsMap = translations.stream()
                .collect(Collectors.toMap(
                        t -> t.getLocale() + "." + t.getTranslationKey(),
                        Translation::getContent
                ));

        List<String> allTags = translations.stream()
                .flatMap(t -> t.getTags().stream())
                .map(tag -> tag.getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return TranslationExportResponse.builder()
                .locale("all")
                .translations(translationsMap)
                .tags(allTags)
                .totalCount(translations.size())
                .exportedAt(LocalDateTime.now().toString())
                .build();
    }
}
