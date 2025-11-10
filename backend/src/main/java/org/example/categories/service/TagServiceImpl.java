package org.example.categories.service;

import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.IdListRequest;
import org.example.categories.dto.request.TagCreateRequest;
import org.example.categories.dto.request.TagUpdateRequest;
import org.example.categories.dto.response.TagResponse;
import org.example.categories.mapper.TagMapper;
import org.example.categories.model.Product;
import org.example.categories.model.Tag;
import org.example.categories.repository.ProductRepository;
import org.example.categories.repository.TagRepository;
import org.example.common.enums.status.TagStatus;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.ConflictException;
import org.example.common.exception.DuplicateResourceException;
import org.example.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    private final TagMapper mapper;
    @Override
    public TagResponse create(TagCreateRequest createRequest) {
        if (tagRepository.existsBySlug(createRequest.slug()))
            throw new DuplicateResourceException("{error.tag.slug.exists}");
        if (tagRepository.existsByName(createRequest.name()))
            throw new DuplicateResourceException("{error.tag.name.exists}");
        Tag tag = mapper.toEntity(createRequest);
        tag = tagRepository.saveAndFlush(tag);
        return mapper.toResponse(tag);
    }

    @Override
    public TagResponse update(Long id,TagUpdateRequest updateRequest) {
        if (tagRepository.existsBySlugAndIdNot(updateRequest.slug(),id))
            throw new DuplicateResourceException("{error.tag.slug.exists}");
        if (tagRepository.existsByNameAndIdNot(updateRequest.name(),id))
            throw new DuplicateResourceException("{error.tag.name.exists}");
        Tag tag = tagRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("{error.tag.id.exists}"));
        mapper.merge(updateRequest,tag);
        tag = tagRepository.save(tag);
        return mapper.toResponse(tag);
    }

    @Override
    public TagResponse findById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("{error.tag.id.exists}"));
        return mapper.toResponse(tag);
    }

    @Override
    public TagResponse findBySlug(String slug) {
        Tag tag = tagRepository.findBySlugAndStatus(slug, TagStatus.ACTIVE)
                .orElseThrow(()-> new NotFoundException("{error.tag.id.exists}"));
        return mapper.toResponse(tag);
    }

    @Override
    public Slice<TagResponse> findByName(String name, Pageable pageable) {
        Slice<Tag> page = tagRepository.findByNameAndStatus(name,TagStatus.ACTIVE,pageable);
        return page.map(mapper::toResponse);
    }

    @Override
    public Slice<TagResponse> findByStatus(TagStatus status, Pageable pageable) {
        Slice<Tag> page = tagRepository.findByStatus(status,pageable);

        return page.map(mapper::toResponse);
    }

    @Override
    public Integer deleteById(IdListRequest request) {
        List<Long> ids = request.ids();
        if (ids == null || ids.isEmpty()) throw new BadRequestException("{error.ids.empty}");

        int ok = tagRepository.countByIdInAndStatus(ids, TagStatus.ACTIVE);
        if (ok != ids.size()) throw new BadRequestException("{error.tag.bulk-delete.invalid-ids}");

        int rows = tagRepository.updateStatusByIdIn(ids, TagStatus.DELETED);
            if (rows != ids.size()) throw new ConflictException("{error.tag.bulk-delete.race-condition}");
        return rows;
    }

    @Override
    public Integer restoreById(IdListRequest request) {
        List<Long> ids = request.ids();
        if (ids == null || ids.isEmpty()) throw new BadRequestException("{error.ids.empty}");

        int ok = tagRepository.countByIdInAndStatus(ids, TagStatus.DELETED);
        if (ok != ids.size()) throw new BadRequestException("{error.tag.bulk-delete.invalid-ids}");

        int rows = tagRepository.updateStatusByIdIn(ids, TagStatus.ACTIVE);
        if (rows != ids.size()) throw new ConflictException("{error.tag.bulk-delete.race-condition}");
        return rows;
    }
}
