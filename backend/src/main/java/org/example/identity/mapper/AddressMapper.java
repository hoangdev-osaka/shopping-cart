package org.example.identity.mapper;

import org.example.identity.dto.request.AddressCreateRequest;
import org.example.identity.dto.request.AddressUpdateRequest;
import org.example.identity.dto.request.UserUpdateRequest;
import org.example.identity.dto.response.AddressResponse;
import org.example.identity.model.Address;
import org.example.identity.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressCreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AddressUpdateRequest dto, @MappingTarget Address entity);

    AddressResponse toResponse(Address entity);
}
