package org.example.categories.mapper;

import org.example.categories.dto.request.CategoryCreateRequest;
import org.example.categories.dto.request.CategoryUpdateRequest;
import org.example.categories.dto.response.CategoryResponse;
import org.example.categories.model.Category;
import org.example.categories.repository.CategoryRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface CategoryMapper {
    Category toEntity(CategoryCreateRequest createRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(CategoryUpdateRequest updateRequest, @MappingTarget Category category);
    CategoryResponse toResponse(Category category);
}
