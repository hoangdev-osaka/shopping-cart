package org.example.categories.controller.admin;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.ProductImageCreateRequest;
import org.example.categories.dto.request.ProductImageUpdateRequest;
import org.example.categories.dto.response.ProductImageResponse;
import org.example.categories.model.Product;
import org.example.categories.service.ProductImageService;
import org.example.categories.service.ProductService;
import org.example.common.enums.status.ProductImageStatus;
import org.example.common.enums.status.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService service;

    @PostMapping("/products/{productId}/images")
    public ResponseEntity<Map<String, Object>> createForProduct(
            @PathVariable Long productId,
            @RequestBody @Valid List<ProductImageCreateRequest> requestList) {

        List<ProductImageResponse> items = service.createForProduct(productId, requestList);

        List<Long> createdIds = items.stream()
                .map(ProductImageResponse::id)
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

        Map<String, Object> body = Map.of(
                "createdCount", createdIds.size(),
                "createdIds", createdIds,
                "createdUris", createdUris,
                "items", items
        );

        return ResponseEntity.created(location).body(body);
    }
    @PatchMapping("/product-images/{id}")
    public ResponseEntity<ProductImageResponse> update(@PathVariable("id") Long updateId,@RequestBody ProductImageUpdateRequest updateRequest) {
        ProductImageResponse productImageResponse =  service.update(updateId, updateRequest);
        return ResponseEntity.ok(productImageResponse);
    }

    @PatchMapping("/product-images/{id}/")
    public ResponseEntity<Void> updateStatusById(@PathVariable Long id,@RequestParam ProductImageStatus status) {
        service.updateStatusById(id, status);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/product-images")
    public ResponseEntity<Slice<ProductImageResponse>> findByStatus(@RequestParam(required = false)ProductImageStatus status,Pageable pageable){
        Slice<ProductImageResponse> slice = service.findByStatus(status,pageable);
        return ResponseEntity.ok(slice);
    }
    @GetMapping("/product-images/{id}")
    public ResponseEntity<ProductImageResponse> findById(@PathVariable Long id) {
        ProductImageResponse productImageResponse =  service.findById(id);
        return ResponseEntity.ok(productImageResponse);
    }
    @GetMapping("/product/{productId}/images")
    public ResponseEntity<Set<ProductImageResponse>> findByProductId(
            @PathVariable Long productId,
            @PageableDefault(
                    page = 0,
                    size = 15,
                    sort = {"isMain", "id"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        Set<ProductImageResponse> responsePage  = service.findByProductId(productId);
        return ResponseEntity.ok(responsePage);
    }
}
