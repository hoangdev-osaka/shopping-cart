package org.example.categories.service;

import org.example.categories.dto.request.IdListRequest;
import org.example.categories.dto.request.ProductCreateRequest;
import org.example.categories.dto.request.ProductUpdateRequest;
import org.example.categories.dto.response.ProductResponse;
import org.example.common.enums.status.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import java.math.BigDecimal;

public interface ProductService {
    //create
    ProductResponse create(ProductCreateRequest createRequest);
    //update
    ProductResponse update(Long productId, ProductUpdateRequest updateRequest);
    //read
    ProductResponse findById(Long id);
    ProductResponse findByName(String name);
    ProductResponse findBySlug(String slug);
    //read list

    Slice<ProductResponse> findByStatus(ProductStatus status, Pageable pageable);
    Slice<ProductResponse> findByBrandId(Long brandId, Pageable pageable);
    Slice<ProductResponse> findByCategoryId(Long CategoryId, Pageable pageable);
    Slice<ProductResponse> findByFilter(
            String keyword,
            ProductStatus status,
            Long brandId,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);
    //delete && restore
    int deleteById(IdListRequest ids);
    int restoreById(IdListRequest ids);


}
