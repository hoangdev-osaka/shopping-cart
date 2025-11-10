package org.example.categories.mapper;

import org.example.categories.dto.request.TagCreateRequest;
import org.example.categories.dto.request.TagUpdateRequest;
import org.example.categories.dto.response.TagResponse;
import org.example.categories.model.Tag;
import org.example.categories.repository.TagRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")

public interface TagMapper {
    Tag toEntity(TagCreateRequest tagCreateRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(TagUpdateRequest tagUpdateRequest,@MappingTarget Tag tag);
    TagResponse toResponse(Tag tag);
}
