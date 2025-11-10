package org.example.categories.controller.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.categories.dto.response.ProductResponse;
import org.example.categories.dto.response.TagResponse;
import org.example.categories.service.ProductTagService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product-tags")
@RequiredArgsConstructor
public class ProductTagController {
    private final ProductTagService productTagService;
    @PostMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> attachTagsToProduct(@PathVariable Long productId, @RequestBody List<Long> tagIds) {
        ProductResponse productResponse = productTagService.attachTagsToProduct(productId, tagIds);
        URI location = URI.create("/api/product-tag/"+productResponse.id());
        return ResponseEntity.created(location).body(productResponse);
    }
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> detachTagsFromProduct(@PathVariable Long productId,@RequestBody List<Long> tagIds) {
        ProductResponse productResponse = productTagService.detachTagsFromProduct(productId, tagIds);
        return ResponseEntity.ok(productResponse);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<Slice<TagResponse>> findTagsByProductId(@PathVariable Long productId, Pageable pageable) {
        Slice<TagResponse> slice = productTagService.findTagsByProductId(productId, pageable);
        return ResponseEntity.ok(slice);
    }
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<Slice<ProductResponse>> findProductsByTagId(@PathVariable Long tagId, Pageable pageable) {
        Slice<ProductResponse> slice = productTagService.findProductsByTagId(tagId, pageable);
        return ResponseEntity.ok(slice);

    }
}
