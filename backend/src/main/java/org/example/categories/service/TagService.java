package org.example.categories.service;

import org.example.categories.dto.request.IdListRequest;
import org.example.categories.dto.request.TagCreateRequest;
import org.example.categories.dto.request.TagUpdateRequest;
import org.example.categories.dto.response.TagResponse;
import org.example.categories.model.Tag;
import org.example.common.enums.status.TagStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TagService {

    // create && update
    TagResponse create(TagCreateRequest createRequest);
    TagResponse update(Long id,TagUpdateRequest updateRequest);
    // read && search list
    TagResponse findById(Long id);
    TagResponse findBySlug(String slug);
    Slice<TagResponse> findByName(String name, Pageable pageable);
    Slice<TagResponse> findByStatus(TagStatus status, Pageable pageable);

    // delete && restore
    Integer deleteById(IdListRequest ids);
    Integer restoreById(IdListRequest ids);

}
