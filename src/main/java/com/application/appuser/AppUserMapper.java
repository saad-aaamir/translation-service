package com.application.appuser;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUserDto toDto(AppUser customer);

    AppUser toEntity(AppUserDto customerPostDTO);

}

