package com.application.mapper;


import com.application.dto.request.TagCreateRequest;
import com.application.dto.response.TagResponse;
import com.application.entity.Tag;
import org.mapstruct.*;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag toEntity(TagCreateRequest request);

    TagResponse toResponse(Tag entity);

    List<TagResponse> toResponseList(List<Tag> entities);

    Set<TagResponse> toResponseSet(Set<Tag> entities);

    void updateEntityFromRequest(@MappingTarget Tag entity, TagCreateRequest request);

    // 👇 Add this method
    default Tag toEntity(String tagName) {
        if (tagName == null) {
            return null;
        }
        return Tag.builder().name(tagName).build();
    }

    // Optional reverse mapping
    default String toName(Tag tag) {
        return tag != null ? tag.getName() : null;
    }
}
