package org.example.categories.mapper;

import org.example.categories.dto.request.BrandCreateRequest;
import org.example.categories.dto.request.BrandUpdateRequest;
import org.example.categories.dto.response.BrandResponse;
import org.example.categories.model.Brand;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toEntity(BrandCreateRequest brandCreateRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(BrandUpdateRequest brandUpdateRequest,@MappingTarget Brand brand);
    BrandResponse toResponse(Brand brand);
}
