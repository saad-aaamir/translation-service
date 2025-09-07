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
}
