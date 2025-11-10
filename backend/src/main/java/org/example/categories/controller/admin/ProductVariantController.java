package org.example.categories.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.ProductVariantCreateRequest;
import org.example.categories.dto.request.ProductVariantUpdateRequest;
import org.example.categories.dto.response.ProductVariantResponse;
import org.example.categories.service.ProductVariantService;
import org.example.common.enums.status.ProductVariantStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService service;
    @PostMapping("products/{productId}")
    public ResponseEntity<Map<String,Object>> create(
            @PathVariable Long productId,
            @RequestBody @Valid List<ProductVariantCreateRequest> requestList) {
        List<ProductVariantResponse> items = service.create(productId, requestList);
        List<Long> createdIds = items.stream()
                .map(ProductVariantResponse::id)
                .toList();
        List<String> createdUris = createdIds.stream()
                .map(id -> ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(id)
                        .toUriString())
                .toList();
        URI location = createdIds.isEmpty()
                ? ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()
                : ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdIds.get(0))
                .toUri();
        Map<String,Object> body = Map.of(
                "createdCount", createdIds.size(),
                "createdIds", createdIds,
                "createdUris", createdUris,
                "items", items
        );
        return ResponseEntity.created(location).body(body);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ProductVariantResponse> update(@PathVariable Long id, @RequestBody ProductVariantUpdateRequest updateRequest) {
        ProductVariantResponse response = service.update(id, updateRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantResponse> findById(@PathVariable Long id) {
        ProductVariantResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/product-id/{id}")
    public ResponseEntity<Slice<ProductVariantResponse>> findByProductId(@PathVariable Long id, Pageable pageable) {
        Slice<ProductVariantResponse> page = service.findByProductId(id, pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping
    public ResponseEntity<Slice<ProductVariantResponse>> findByFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) ProductVariantStatus status,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable) {
        Slice<ProductVariantResponse> page = service.findByFilter(keyword, color, size, status, productId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(page);
    }
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
    @PatchMapping("/restore/{id}")
    public ResponseEntity<Boolean> restoreById(@PathVariable Long id) {
        return ResponseEntity.ok(service.restoreById(id));
    }
}
