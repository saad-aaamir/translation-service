package com.application.mapper;

import com.application.dto.request.TranslationCreateRequest;
import com.application.dto.request.TranslationUpdateRequest;
import com.application.dto.response.TranslationResponse;
import com.application.entity.Translation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface TranslationMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "tags", source = "tagNames")
    })
    Translation toEntity(TranslationCreateRequest request);

    TranslationResponse toResponse(Translation entity);

    List<TranslationResponse> toResponseList(List<Translation> entities);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "translationKey", ignore = true),
            @Mapping(target = "locale", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "tags", ignore = true)
    })
    void updateEntityFromRequest(@MappingTarget Translation entity, TranslationUpdateRequest request);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "tags", ignore = true)
    })
    void updateEntityFromCreateRequest(@MappingTarget Translation entity, TranslationCreateRequest request);
}
