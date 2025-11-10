package org.example.categories.controller.admin;


import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.BrandCreateRequest;
import org.example.categories.dto.request.BrandUpdateRequest;
import org.example.categories.dto.response.BrandResponse;
import org.example.categories.service.BrandService;
import org.example.common.enums.status.BrandStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    @PostMapping
    public ResponseEntity<BrandResponse> create(@RequestBody BrandCreateRequest createRequest) {
        BrandResponse brandResponse =  brandService.create(createRequest);
        URI location = URI.create("/api/brands/"+brandResponse.id());
        return ResponseEntity.created(location).body(brandResponse);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatusById(@PathVariable Long id,@RequestParam BrandStatus status) {
        brandService.updateStatusById(id, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BrandResponse> update(@PathVariable Long id,@RequestBody BrandUpdateRequest updateRequest) {
        BrandResponse updated = brandService.update(id, updateRequest);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(@PathVariable Long id) {
        BrandResponse brandResponse =  brandService.findById(id);
        return ResponseEntity.ok(brandResponse);
    }
    @GetMapping("/name")
    public ResponseEntity<BrandResponse> findByName(@RequestParam String name) {
        BrandResponse brandResponse = brandService.findByName(name);
        return ResponseEntity.ok(brandResponse);
    }
    @GetMapping("/slug")
    public ResponseEntity<BrandResponse> findBySlug(@RequestParam String slug) {
        BrandResponse brandResponse = brandService.findBySlug(slug);
        return ResponseEntity.ok(brandResponse);
    }
    @GetMapping
    public ResponseEntity<Slice<BrandResponse>> findByStatus(
            @RequestParam(required = false) BrandStatus status,
            @PageableDefault(
                    page = 0,
                    size = 15,
                    sort = {"createdAt","id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        Slice<BrandResponse> brandResponsePage =  brandService.findByStatus(status, pageable);
        return ResponseEntity.ok(brandResponsePage);
    }
}
