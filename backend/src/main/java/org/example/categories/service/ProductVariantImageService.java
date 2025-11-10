package org.example.categories.service;

import org.example.categories.dto.request.*;
import org.example.categories.dto.response.ProductImageResponse;
import org.example.categories.dto.response.ProductVariantImageResponse;
import org.example.categories.model.ProductVariantImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductVariantImageService {
    //create
    ProductVariantImageResponse create(ProductVariantImageCreateRequest createRequest);
    //update
    ProductVariantImageResponse update(Long updateId, ProductVariantImageUpdateRequest updateRequest);
    //read
    ProductVariantImageResponse findById(Long id);
    //read list
    Slice<ProductVariantImageResponse> findByProductVariantIdAndColorAndSizeAndStatus(Long id, String color, String size, Pageable pageable);
    //delete && restore
    Integer deleteById(IdListRequest ids);
    Integer restoreById(IdListRequest ids);
}
