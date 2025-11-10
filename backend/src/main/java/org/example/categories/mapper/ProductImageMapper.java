package org.example.categories.mapper;

import org.example.categories.dto.request.ProductImageCreateRequest;
import org.example.categories.dto.request.ProductImageUpdateRequest;
import org.example.categories.dto.request.ProductVariantCreateRequest;
import org.example.categories.dto.request.ProductVariantImageCreateRequest;
import org.example.categories.dto.response.ProductImageResponse;
import org.example.categories.model.ProductImage;
import org.example.categories.model.ProductVariantImage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")

public interface ProductImageMapper {
    ProductImage toEntity(ProductImageCreateRequest createRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(ProductImageUpdateRequest updateRequest, @MappingTarget ProductImage productImage);
    ProductImageResponse toResponse(ProductImage productImage);
}
