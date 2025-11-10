package org.example.categories.mapper;

import org.example.categories.dto.request.ProductVariantImageCreateRequest;
import org.example.categories.dto.request.ProductVariantImageUpdateRequest;
import org.example.categories.dto.response.ProductVariantImageResponse;
import org.example.categories.model.ProductVariantImage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface ProductVariantImageMapper {
    ProductVariantImage toEntity(ProductVariantImageCreateRequest createRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(ProductVariantImageUpdateRequest updateRequest, @MappingTarget ProductVariantImage productVariantImage);
    ProductVariantImageResponse toResponse(ProductVariantImage productVariantImage);
}
