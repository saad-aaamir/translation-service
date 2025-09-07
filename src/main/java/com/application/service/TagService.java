package com.application.service;

import com.application.dto.request.TagCreateRequest;
import com.application.dto.response.TagResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TagService {
    TagResponse createTag(TagCreateRequest request);
    TagResponse updateTag(Long id, TagCreateRequest request);
    TagResponse getTagById(Long id);
    TagResponse getTagByName(String name);
    List<TagResponse> getAllTags();
    Page<TagResponse> getAllTags(Pageable pageable);
    List<TagResponse> searchTagsByName(String name);
    void deleteTag(Long id);
    boolean existsByName(String name);
    List<TagResponse> getMostUsedTags(int limit);
}
