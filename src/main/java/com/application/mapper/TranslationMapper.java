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

    Translation toEntity(TranslationCreateRequest request);

    TranslationResponse toResponse(Translation entity);

    List<TranslationResponse> toResponseList(List<Translation> entities);

    void updateEntityFromRequest(@MappingTarget Translation entity, TranslationUpdateRequest request);

    void updateEntityFromCreateRequest(@MappingTarget Translation entity, TranslationCreateRequest request);
}
