package com.application.service.impl;

import com.application.dto.request.TranslationCreateRequest;
import com.application.dto.request.TranslationSearchRequest;
import com.application.dto.request.TranslationUpdateRequest;
import com.application.dto.response.TranslationResponse;
import com.application.entity.Tag;
import com.application.entity.Translation;
import com.application.exception.DuplicateResourceException;
import com.application.exception.TranslationNotFoundException;
import com.application.mapper.TranslationMapper;
import com.application.repository.TagRepository;
import com.application.repository.TranslationRepository;
import com.application.service.TranslationService;
import com.application.specification.TranslationSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;
    private final TagRepository tagRepository;
    private final TranslationMapper translationMapper;

    @Override
    public TranslationResponse createTranslation(TranslationCreateRequest request) {
        log.debug("Creating translation with key: {} and locale: {}", request.getTranslationKey(), request.getLocale());

        if (translationRepository.findByTranslationKeyAndLocale(request.getTranslationKey(), request.getLocale()).isPresent()) {
            throw new DuplicateResourceException(request.getTranslationKey(), request.getLocale());
        }

        Translation translation = translationMapper.toEntity(request);

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            Set<Tag> tags = findOrCreateTags(request.getTagNames());
            translation.setTags(tags);
        }

        Translation saved = translationRepository.save(translation);
        log.info("Translation created with id: {}", saved.getId());
        return translationMapper.toResponse(saved);
    }

    @Override
    @CacheEvict(value = "translations", key = "#id")
    public TranslationResponse updateTranslation(Long id, TranslationUpdateRequest request) {
        log.debug("Updating translation with id: {}", id);

        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new TranslationNotFoundException(id));

        translationMapper.updateEntityFromRequest(translation, request);

        // Update tags
        if (request.getTagNames() != null) {
            translation.getTags().clear();
            if (!request.getTagNames().isEmpty()) {
                Set<Tag> tags = findOrCreateTags(request.getTagNames());
                translation.setTags(tags);
            }
        }

        Translation saved = translationRepository.save(translation);
        log.info("Translation updated with id: {}", saved.getId());
        return translationMapper.toResponse(saved);
    }

    @Override
    @Cacheable(value = "translations", key = "#id")
    @Transactional(readOnly = true)
    public TranslationResponse getTranslationById(Long id) {
        log.debug("Fetching translation with id: {}", id);

        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new TranslationNotFoundException(id));

        return translationMapper.toResponse(translation);
    }

    @Override
    @Cacheable(value = "translationsByKeyLocale", key = "#key + '_' + #locale")
    @Transactional(readOnly = true)
    public TranslationResponse getTranslationByKeyAndLocale(String key, String locale) {
        log.debug("Fetching translation with key: {} and locale: {}", key, locale);

        Translation translation = translationRepository.findByTranslationKeyAndLocale(key, locale)
                .orElseThrow(() -> new TranslationNotFoundException(key, locale));

        return translationMapper.toResponse(translation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TranslationResponse> searchTranslations(TranslationSearchRequest request) {
        log.debug("Searching translations with criteria: {}", request);

        Pageable pageable = createPageable(request);
        Specification<Translation> spec = TranslationSpecification.withCriteria(request);

        Page<Translation> translations = translationRepository.findAll(spec, pageable);
        return translations.map(translationMapper::toResponse);
    }

    @Override
    @Cacheable(value = "translationsByLocale", key = "#locale")
    @Transactional(readOnly = true)
    public List<TranslationResponse> getTranslationsByLocale(String locale) {
        log.debug("Fetching translations for locale: {}", locale);

        List<Translation> translations = translationRepository.findByLocale(locale);
        return translationMapper.toResponseList(translations);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TranslationResponse> getTranslationsByTag(String tagName) {
        log.debug("Fetching translations for tag: {}", tagName);

        List<Translation> translations = translationRepository.findByTagName(tagName);
        return translationMapper.toResponseList(translations);
    }

    @Override
    @CacheEvict(value = {"translations", "translationsByKeyLocale", "translationsByLocale"}, allEntries = true)
    public void deleteTranslation(Long id) {
        log.debug("Deleting translation with id: {}", id);

        if (!translationRepository.existsById(id)) {
            throw new TranslationNotFoundException(id);
        }

        translationRepository.deleteById(id);
        log.info("Translation deleted with id: {}", id);
    }

    @Override
    @CacheEvict(value = {"translations", "translationsByKeyLocale", "translationsByLocale"}, allEntries = true)
    public void deleteTranslationsByLocale(String locale) {
        log.debug("Deleting translations for locale: {}", locale);

        translationRepository.deleteByLocale(locale);
        log.info("Translations deleted for locale: {}", locale);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTranslationsByLocale(String locale) {
        return translationRepository.countByLocale(locale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TranslationResponse> fullTextSearch(String searchTerm) {
        log.debug("Full-text search for term: {}", searchTerm);

        List<Translation> translations = translationRepository.fullTextSearch(searchTerm);
        return translationMapper.toResponseList(translations);
    }

    private Set<Tag> findOrCreateTags(Set<String> tagNames) {
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build())))
                .collect(Collectors.toSet());
    }

    private Pageable createPageable(TranslationSearchRequest request) {
        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(request.getSortDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                request.getSortBy()
        );
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}