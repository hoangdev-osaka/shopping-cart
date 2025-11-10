package org.example.categories.controller.admin;


import lombok.RequiredArgsConstructor;
import org.example.categories.dto.request.IdListRequest;
import org.example.categories.dto.request.TagCreateRequest;
import org.example.categories.dto.request.TagUpdateRequest;
import org.example.categories.dto.response.TagResponse;
import org.example.categories.service.TagService;
import org.example.common.enums.status.TagStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;
    @PostMapping
    public ResponseEntity<TagResponse> create(@RequestBody TagCreateRequest createRequest) {
        TagResponse response = service.create(createRequest);
        URI location = URI.create("api/tags/"+response.id());
        return ResponseEntity.created(location).body(response);
    }
    @PatchMapping("{id}")
    public ResponseEntity<TagResponse> update(@PathVariable Long id, @RequestBody TagUpdateRequest updateRequest) {
        TagResponse response = service.update(id, updateRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<TagResponse> findById(@PathVariable Long id) {
        TagResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/slug")
    public ResponseEntity<TagResponse> findBySlug(@RequestParam String slug) {
        TagResponse response = service.findBySlug(slug);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<Slice<TagResponse>> findByStatus(@RequestParam(required = false) TagStatus status, Pageable pageable) {
        Slice<TagResponse> page = service.findByStatus(status, pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/name")
    public ResponseEntity<Slice<TagResponse>> findByName(@RequestParam String name, Pageable pageable) {
        Slice<TagResponse> page = service.findByName(name, pageable);
        return ResponseEntity.ok(page);
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
