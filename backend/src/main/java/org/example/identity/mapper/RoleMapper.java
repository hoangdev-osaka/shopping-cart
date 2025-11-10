package org.example.identity.mapper;

import org.example.identity.dto.request.RoleCreateRequest;
import org.example.identity.dto.request.RoleUpdateRequest;
import org.example.identity.dto.response.RoleResponse;
import org.example.identity.model.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleCreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RoleUpdateRequest dto, @MappingTarget Role entity);

    RoleResponse toResponse(Role entity);

}
