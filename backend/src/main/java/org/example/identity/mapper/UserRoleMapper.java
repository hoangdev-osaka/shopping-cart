package org.example.identity.mapper;


import org.example.identity.dto.response.UserRoleResponse;
import org.example.identity.model.UserRole;
import org.example.identity.repository.UserRoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {
    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "role.id",target = "roleId")
    UserRoleResponse toResponse(UserRole entity);
}
