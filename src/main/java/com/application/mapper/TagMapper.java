package com.application.mapper;

import com.application.dto.request.TagCreateRequest;
import com.application.dto.response.TagResponse;
import com.application.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public Tag toEntity(TagCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Tag.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public TagResponse toResponse(Tag entity) {
        if (entity == null) {
            return null;
        }
        return TagResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public List<TagResponse> toResponseList(List<Tag> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Set<TagResponse> toResponseSet(Set<Tag> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toSet());
    }

    public void updateEntityFromRequest(Tag entity, TagCreateRequest request) {
        if (request == null || entity == null) {
            return;
        }
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
    }

    public Tag toEntity(String tagName) {
        if (tagName == null) {
            return null;
        }
        return Tag.builder().name(tagName).build();
    }

    public String toName(Tag tag) {
        return tag != null ? tag.getName() : null;
    }
}
