package org.example.categories.service;


import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.BrandCreateRequest;
import org.example.categories.dto.request.BrandUpdateRequest;
import org.example.categories.dto.response.BrandResponse;
import org.example.categories.mapper.BrandMapper;
import org.example.categories.model.Brand;
import org.example.categories.repository.BrandRepository;
import org.example.common.enums.status.BrandStatus;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.DuplicateResourceException;
import org.example.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.basic.BasicSliderUI;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandResponse create(BrandCreateRequest createRequest) {
        if (brandRepository.existsByName(createRequest.name()))
            throw new DuplicateResourceException("{error.brand.name.exists}");
        if (brandRepository.existsBySlug(createRequest.slug()))
            throw new DuplicateResourceException("{error.brand.slug.exists}");
        Brand brand = brandMapper.toEntity(createRequest);
        brand = brandRepository.save(brand);
        return brandMapper.toResponse(brand);
    }

    @Override
    public BrandResponse update(Long id, BrandUpdateRequest updateRequest) {
        if (brandRepository.existsByNameAndIdNot(updateRequest.name(),id))
            throw new DuplicateResourceException("{error.brand.name.exists}");
        if (brandRepository.existsBySlugAndIdNot(updateRequest.slug(),id))
            throw new DuplicateResourceException("{error.brand.slug.exists}");
        Brand brand = brandRepository.findById(id)
                .orElseThrow(()->new NotFoundException("{error.brand.id.notFound}"));
        brandMapper.merge(updateRequest,brand);
        brand = brandRepository.save(brand);
        return brandMapper.toResponse(brand);
    }

    @Override
    public void updateStatusById(Long id, BrandStatus status) {
        int updatedRow = brandRepository.updateStatusById(id,status);
        if (updatedRow == 0)
            throw new NotFoundException("{error.brand.id.notFound}");
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse findById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("{error.brand.id.notFound}"));
        return brandMapper.toResponse(brand);
    }

    @Override
    @Transactional(readOnly = true)

    public BrandResponse findByName(String name) {
        Brand brand = brandRepository.findByName(name)
                .orElseThrow(()-> new NotFoundException("{error.brand.name.notFound}"));
        return brandMapper.toResponse(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse findBySlug(String slug) {
        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(()-> new NotFoundException("{error.brand.slug.notFound}"));
        return brandMapper.toResponse(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<BrandResponse> findByStatus(BrandStatus status, Pageable pageable) {
        Slice<Brand> brands = brandRepository.findByStatus(status,pageable);
        return brands.map(brandMapper::toResponse);

    }
}
