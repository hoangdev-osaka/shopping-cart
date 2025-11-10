package org.example.categories.controller.admin;


import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.IdListRequest;
import org.example.categories.dto.request.ProductVariantImageCreateRequest;
import org.example.categories.dto.request.ProductVariantImageUpdateRequest;
import org.example.categories.dto.response.ProductVariantImageResponse;
import org.example.categories.model.ProductVariantImage;
import org.example.categories.service.ProductVariantImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/product-variant-images")
@RequiredArgsConstructor
public class ProductVariantImageController {
    private final ProductVariantImageService service;
    @PostMapping
    public ResponseEntity<ProductVariantImageResponse> create(ProductVariantImageCreateRequest createRequest) {
        ProductVariantImageResponse response = service.create(createRequest);
        URI location = URI.create("/api/product-variant-image/"+response.id());
        return ResponseEntity.created(location).body(response);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ProductVariantImageResponse> update(@PathVariable Long id,@RequestBody ProductVariantImageUpdateRequest updateRequest) {
        ProductVariantImageResponse response = service.update(id, updateRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantImageResponse> findById(@PathVariable Long id) {
        ProductVariantImageResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public Slice<ProductVariantImageResponse> findByProductVariantIdAndColorAndSizeAndStatus(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            Pageable pageable) {
        return service.findByProductVariantIdAndColorAndSizeAndStatus(id, color, size, pageable);
    }
    @PatchMapping("/delete")
    public ResponseEntity<Integer> deleteById(@RequestBody IdListRequest ids) {
        return ResponseEntity.ok(service.deleteById(ids));
    }
    @PatchMapping("/restore")
    public ResponseEntity<Integer> restoreById(@RequestBody IdListRequest ids) {
        return ResponseEntity.ok(service.restoreById(ids));
    }
}
