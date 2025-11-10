package org.example.categories.service;

import lombok.extern.java.Log;
import org.example.categories.dto.request.CategoryCreateRequest;
import org.example.categories.dto.request.CategoryUpdateRequest;
import org.example.categories.dto.request.IdListRequest;
import org.example.categories.dto.response.CategoryResponse;
import org.example.categories.dto.response.TagResponse;
import org.example.common.enums.status.CategoryStatus;
import org.hibernate.cache.spi.SecondLevelCacheLogger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CategoryService {
    //create
    CategoryResponse create(CategoryCreateRequest createRequest);
    //update
    CategoryResponse update(Long categoryId, CategoryUpdateRequest updateRequest);
    CategoryResponse replaceParent(Long categoryId, Long parentId);
    CategoryResponse unsetParent(Long categoryId);
    //read
    CategoryResponse findById(Long id);
    CategoryResponse findByName(String name);
    CategoryResponse findBySlug(String slug);
    //read list
    Slice<CategoryResponse> findAllByStatus(CategoryStatus status, Pageable pageable);
    //update status
    boolean updateStatusById(Long id , CategoryStatus categoryStatus);


    Slice<CategoryResponse> findByProductId(Long id, Pageable pageable);

    CategoryResponse replaceProducts(Long categoryId, IdListRequest ids);

    CategoryResponse unsetProducts(Long categoryId, IdListRequest ids);
}
