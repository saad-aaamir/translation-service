package com.application.service.impl;

import com.application.dto.request.TagCreateRequest;
import com.application.dto.response.TagResponse;
import com.application.entity.Tag;
import com.application.exception.DuplicateResourceException;
import com.application.exception.TagNotFoundException;
import com.application.mapper.TagMapper;
import com.application.repository.TagRepository;
import com.application.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;



    @Override
    public TagResponse createTag(TagCreateRequest request) {
        log.debug("Creating tag with name: {}", request.getName());

        if (tagRepository.existsByName(request.getName())) {
            throw DuplicateResourceException.forTag(request.getName());
        }

        Tag tag = tagMapper.toEntity(request);
        Tag saved = tagRepository.save(tag);

        log.info("Tag created with id: {}", saved.getId());
        return tagMapper.toResponse(saved);
    }

    @Override
    @CacheEvict(value = "tags", key = "#id")
    public TagResponse updateTag(Long id, TagCreateRequest request) {
        log.debug("Updating tag with id: {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        // Check for name conflict with other tags
        tagRepository.findByName(request.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw DuplicateResourceException.forTag(request.getName());
                });

        tagMapper.updateEntityFromRequest(tag, request);
        Tag saved = tagRepository.save(tag);

        log.info("Tag updated with id: {}", saved.getId());
        return tagMapper.toResponse(saved);
    }

    @Override
    @Cacheable(value = "tags", key = "#id")
    @Transactional(readOnly = true)
    public TagResponse getTagById(Long id) {
        log.debug("Fetching tag with id: {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));

        return tagMapper.toResponse(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public TagResponse getTagByName(String name) {
        log.debug("Fetching tag with name: {}", name);

        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> TagNotFoundException.byName(name));

        return tagMapper.toResponse(tag);
    }

    @Override
    @Cacheable(value = "allTags")
    @Transactional(readOnly = true)
    public List<TagResponse> getAllTags() {
        log.debug("Fetching all tags");

        List<Tag> tags = tagRepository.findAll();
        return tagMapper.toResponseList(tags);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TagResponse> getAllTags(Pageable pageable) {
        log.debug("Fetching tags with pagination: {}", pageable);

        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags.map(tagMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> searchTagsByName(String name) {
        log.debug("Searching tags with name containing: {}", name);

        List<Tag> tags = tagRepository.findByNameContainingIgnoreCase(name);
        return tagMapper.toResponseList(tags);
    }

    @Override
    @CacheEvict(value = {"tags", "allTags"}, allEntries = true)
    public void deleteTag(Long id) {
        log.debug("Deleting tag with id: {}", id);

        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id);
        }

        tagRepository.deleteById(id);
        log.info("Tag deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> getMostUsedTags(int limit) {
        log.debug("Fetching {} most used tags", limit);

        Pageable pageable = PageRequest.of(0, limit);
        Page<Tag> tags = tagRepository.findMostUsedTags(pageable);
        return tagMapper.toResponseList(tags.getContent());
    }
}
