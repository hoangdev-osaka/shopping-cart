package org.example.categories.service;

import org.example.categories.dto.request.BrandCreateRequest;
import org.example.categories.dto.request.BrandUpdateRequest;
import org.example.categories.dto.response.BrandResponse;
import org.example.categories.model.Brand;
import org.example.common.enums.status.BrandStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.awt.*;

public interface BrandService {
    //create
    BrandResponse create(BrandCreateRequest createRequest);
    //update
    BrandResponse update(Long id, BrandUpdateRequest updateRequest);
    void updateStatusById(Long id,BrandStatus status);
    //read
    BrandResponse findById(Long id);

    BrandResponse findByName(String name);

    BrandResponse findBySlug(String slug);

    Slice<BrandResponse> findByStatus(BrandStatus status, Pageable pageable);
}
