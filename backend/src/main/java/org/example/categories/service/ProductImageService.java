package org.example.categories.service;

import jakarta.validation.Valid;
import org.example.categories.dto.request.ProductImageCreateRequest;
import org.example.categories.dto.request.ProductImageUpdateRequest;
import org.example.categories.dto.request.ProductVariantUpdateRequest;
import org.example.categories.dto.response.ProductImageResponse;
import org.example.categories.model.ProductImage;
import org.example.common.enums.status.ProductImageStatus;
import org.example.common.enums.status.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Set;

public interface ProductImageService {
    //create
    ProductImageResponse create(ProductImageCreateRequest createRequest);

    //update
    ProductImageResponse update(Long updateId, ProductImageUpdateRequest updateRequest);

    void updateStatusById(Long id, ProductImageStatus status);

    //read
    ProductImageResponse findById(Long id);

    //read list
    Set<ProductImageResponse> findByProductId(Long productId);

    Slice<ProductImageResponse> findByStatus(ProductImageStatus status,Pageable pageable);

    List<ProductImageResponse> createForProduct(Long productId, @Valid List<ProductImageCreateRequest> requestList);
}



