package org.example.categories.service;

import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.IdListRequest;
import org.example.categories.dto.request.ProductVariantImageCreateRequest;
import org.example.categories.dto.request.ProductVariantImageUpdateRequest;
import org.example.categories.dto.response.ProductVariantImageResponse;
import org.example.categories.dto.response.ProductVariantResponse;
import org.example.categories.mapper.ProductVariantImageMapper;
import org.example.categories.model.ProductVariant;
import org.example.categories.model.ProductVariantImage;
import org.example.categories.repository.ProductVariantImageRepository;
import org.example.categories.repository.ProductVariantRepository;
import org.example.common.enums.status.ProductVariantImageStatus;
import org.example.common.enums.status.ProductVariantStatus;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.DuplicateResourceException;
import org.example.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional@RequiredArgsConstructor
public class ProductVariantImageServiceImpl implements  ProductVariantImageService {
    private final ProductVariantImageRepository productVariantImageRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantImageMapper mapper;
    @Override
    public ProductVariantImageResponse create(ProductVariantImageCreateRequest createRequest) {
        ProductVariant productVariant= productVariantRepository.findById(createRequest.productVariantId())
                .orElseThrow(()-> new NotFoundException("{error.product-variant-image.product-variant-id.not-found}"));
        if (createRequest.isMain() && productVariantImageRepository.existsByProductVariantIdAndIsMainTrue(createRequest.productVariantId())) {
            throw new DuplicateResourceException("{error.product-variant-image.is-main.duplicate}");
        }
        if (productVariant.getStatus() != ProductVariantStatus.ACTIVE)
            throw new BadRequestException("{error.product-variant-image.product-variant-id.bad-request}");
        boolean isMain = createRequest.isMain();
        ProductVariantImage entity = mapper.toEntity(createRequest);
        entity.setProductVariant(productVariant);
        entity = productVariantImageRepository.save(entity);
        return mapper.toResponse(entity);
    }


    @Override
    public ProductVariantImageResponse update(Long updateId, ProductVariantImageUpdateRequest updateRequest) {
        ProductVariantImage productVariantImage = productVariantImageRepository.findById(updateId)
                .orElseThrow(()-> new NotFoundException("{error.product-variant-image.id.not-found}"));
        mapper.merge(updateRequest,productVariantImage);
            productVariantImage = productVariantImageRepository.save(productVariantImage);
        return mapper.toResponse(productVariantImage);
    }

    @Override
    public ProductVariantImageResponse findById(Long id) {
        ProductVariantImage productVariantImage = productVariantImageRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("{error.product-variant-image.id.not-found}"));
        return mapper.toResponse(productVariantImage);
    }

    @Override
    public Slice<ProductVariantImageResponse> findByProductVariantIdAndColorAndSizeAndStatus(Long id, String color, String size, Pageable pageable) {
        Slice<ProductVariantImage> page = productVariantImageRepository
                .findByProductVariantIdAndColorAndSizeAndStatus(id,color,size, ProductVariantImageStatus.ACTIVE,pageable);
        return page.map(mapper::toResponse);
    }

    @Override
    public Integer deleteById(IdListRequest ids) {
        return productVariantImageRepository.updateStatusById(ProductVariantImageStatus.DELETED,ids);
    }

    @Override
    public Integer restoreById(IdListRequest ids) {
        return productVariantImageRepository.updateStatusById(ProductVariantImageStatus.ACTIVE,ids);
    }
}
