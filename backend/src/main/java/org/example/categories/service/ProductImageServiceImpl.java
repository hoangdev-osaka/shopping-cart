package org.example.categories.service;

import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.ProductImageCreateRequest;
import org.example.categories.dto.request.ProductImageUpdateRequest;
import org.example.categories.dto.request.ProductVariantUpdateRequest;
import org.example.categories.dto.response.ProductImageResponse;
import org.example.categories.mapper.ProductImageMapper;
import org.example.categories.model.Product;
import org.example.categories.model.ProductImage;
import org.example.categories.repository.ProductImageRepository;
import org.example.categories.repository.ProductRepository;
import org.example.common.enums.status.ProductImageStatus;
import org.example.common.enums.status.ProductStatus;
import org.example.common.exception.DuplicateResourceException;
import org.example.common.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository repository;
    private final ProductRepository productRepository;
    private final ProductImageMapper mapper;

    @Override
    public ProductImageResponse create(ProductImageCreateRequest createRequest) {
        Product product = productRepository.findById(createRequest.productId())
                .orElseThrow(()-> new NotFoundException("{error.product.id.not-found}"));
        ProductImage productImage = mapper.toEntity(createRequest);
        productImage.setProduct(product);
        productImage =  repository.save(productImage);
        return mapper.toResponse(productImage);
    }

    @Override
    public ProductImageResponse update(Long id, ProductImageUpdateRequest updateRequest) {
        ProductImage productImage = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("{error.product-image.id.not-found}"));
            mapper.merge(updateRequest,productImage);
        return mapper.toResponse(repository.save(productImage));
    }

    @Override
    public void updateStatusById(Long id, ProductImageStatus status) {
        int updatedRow = repository.updateStatusById(status,id);
        if (updatedRow == 0)
            throw new NotFoundException("{error.product.id.not-found}");
    }

    @Override
    public ProductImageResponse findById(Long id) {
        ProductImage productImage = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("{error.product.id.not-found}"));
        return mapper.toResponse(productImage);
    }

    @Override
    public Set<ProductImageResponse> findByProductId(Long id) {
        Set<ProductImage> productImagePage = repository.findByProductId(id);
        return productImagePage.stream().map(mapper::toResponse).collect(Collectors.toSet());
    }

    @Override
    public Slice<ProductImageResponse> findByStatus(ProductImageStatus status,Pageable pageable) {
        Slice<ProductImage> slice = repository.findByStatus(status,pageable);
        return slice.map(mapper::toResponse);
    }

    @Override
    public List<ProductImageResponse> createForProduct(Long productId, List<ProductImageCreateRequest> reqs) {
        if (productId == null) throw new IllegalArgumentException("productId.required");
        if (reqs == null || reqs.isEmpty()) return List.of();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("{error.product.id-not-found}"));

        Function<String,String> norm = s -> Optional.ofNullable(s).orElse("").trim().toLowerCase();

        Set<String> existing = repository.findByProductId(productId).stream()
                .map(pi -> norm.apply(pi.getImageUrl())).collect(Collectors.toSet());

        Set<String> seen = new HashSet<>();
        for (var r : reqs) {
            String key = norm.apply(r.imageUrl());
            if (key.isBlank()) throw new IllegalArgumentException("url.required");
            if (!seen.add(key) || existing.contains(key)) throw new DuplicateResourceException("{error.product-image.url.duplicate}");
        }

        var toSave = reqs.stream().map(r -> {
            var e = mapper.toEntity(r);
            e.setProduct(product);
            return e;
        }).toList();

        try {
            var saved = repository.saveAll(toSave);
            return saved.stream().map(mapper::toResponse).toList();
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException("{error.product-image.duplicate}");
        }
    }


}
