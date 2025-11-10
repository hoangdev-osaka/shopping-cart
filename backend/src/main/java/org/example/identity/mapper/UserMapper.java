package org.example.identity.mapper;

import org.example.identity.dto.request.UserCreateRequest;
import org.example.identity.dto.request.UserUpdateRequest;
import org.example.identity.dto.response.UserResponse;
import org.example.identity.model.User;
import org.mapstruct.*;
// componentModel : kiểu đại diện dữ liêụ thành phần của ai?? Spring
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateRequest dto);
    //nullValuePropertyMappingStrategy : Chiến lược ánh xạ khi gặp thuộc tính có giá trị null
    //IGNORE option này bỏ qua giá trị null
    @BeanMapping(nullValuePropertyMappingStrategy  = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateRequest dto , @MappingTarget User entity);

    UserResponse toResponse(User entity);

}
