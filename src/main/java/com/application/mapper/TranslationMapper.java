package com.application.mapper;

import com.application.dto.request.TranslationCreateRequest;
import com.application.dto.request.TranslationUpdateRequest;
import com.application.dto.response.TranslationResponse;
import com.application.dto.response.TagResponse;
import com.application.entity.Translation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TranslationMapper {

    private final TagMapper tagMapper;

    public TranslationMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public Translation toEntity(TranslationCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new Translation(
                request.getTranslationKey(),
                request.getContent(),
                request.getLocale()
        );
    }

    public TranslationResponse toResponse(Translation entity) {
        if (entity == null) {
            return null;
        }
        
        Set<TagResponse> tagResponses = null;
        if (entity.getTags() != null) {
            tagResponses = entity.getTags().stream()
                    .map(tagMapper::toResponse)
                    .collect(Collectors.toSet());
        }
        
        return TranslationResponse.builder()
                .id(entity.getId())
                .translationKey(entity.getTranslationKey())
                .content(entity.getContent())
                .locale(entity.getLocale())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .tags(tagResponses)
                .build();
    }

    public List<TranslationResponse> toResponseList(List<Translation> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(Translation entity, TranslationUpdateRequest request) {
        if (request == null || entity == null) {
            return;
        }
        entity.setContent(request.getContent());
    }
}
