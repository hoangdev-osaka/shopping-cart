package org.example.categories.mapper;

import org.example.categories.dto.request.ProductVariantCreateRequest;
import org.example.categories.dto.request.ProductVariantUpdateRequest;
import org.example.categories.dto.response.ProductImageResponse;
import org.example.categories.dto.response.ProductVariantResponse;
import org.example.categories.model.ProductImage;
import org.example.categories.model.ProductVariant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface ProductVariantMapper {
    ProductVariant toEntity(ProductVariantCreateRequest createRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merger(ProductVariantUpdateRequest updateRequest,@MappingTarget ProductVariant productVariant);
    ProductVariantResponse toResponse(ProductVariant productVariant);


}
