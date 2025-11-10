package org.example.identity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.identity.dto.request.AddressCreateRequest;
import org.example.identity.dto.request.AddressUpdateRequest;
import org.example.identity.dto.response.AddressResponse;
import org.example.identity.model.Address;
import org.example.identity.service.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/users/{id}")
    public AddressResponse createAddress(@PathVariable("id") Long userId,
            @RequestBody @Valid AddressCreateRequest addressCreateRequest) {
        return addressService.createAddress(userId,addressCreateRequest);
    }
    @GetMapping("/{addressId}")
    public AddressResponse getById(@PathVariable Long addressId) {
        return addressService.getById(addressId);
    }
    @PatchMapping("/{addressId}")
    public AddressResponse updateAddress(@PathVariable Long addressId, AddressUpdateRequest addressUpdateRequest) {
        return addressService.updateAddress(addressId, addressUpdateRequest);
    }
    @PatchMapping("/restore/{addressId}")
    public boolean restoreById(Long addressId) {
        return addressService.restoreById(addressId);
    }
    @DeleteMapping("{addressId}")
    public boolean softDeleteById(@PathVariable Long addressId) {
        return addressService.softDeleteById(addressId);
    }
    @GetMapping("/list-user/{id}")
    public Page<AddressResponse> listByUser(
            @PathVariable("id") Long userId,
            @PageableDefault(
                    page = 0,
                    size = 15,
                    sort = {"id","createdAt"},
                    direction = Sort.Direction.DESC)
            Pageable pageable) {
        return addressService.listByUser(userId, pageable);
    }
    @PutMapping("/users/{userId}/addresses/{addressId}/default")
    public ResponseEntity<Void> setDefault(
            @PathVariable Long userId,
            @PathVariable("addressId") Long addressId) {

        boolean updated = addressService.setDefault(userId, addressId);
        // updated == true: đã set hoặc đã đúng default ⇒ 204 No Content (thực dụng, không body)
        // updated == false: không tìm thấy hoặc không thuộc user ⇒ 404 Not Found
        return updated ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/addresses/default")
    public ResponseEntity<AddressResponse> getDefault(@PathVariable Long userId) {
        AddressResponse res = addressService.getDefault(userId);
        return (res != null) ? ResponseEntity.ok(res)
                : ResponseEntity.noContent().build();
    }
}
