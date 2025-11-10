package org.example.identity.service;

import org.example.identity.dto.request.AddressCreateRequest;
import org.example.identity.dto.request.AddressUpdateRequest;
import org.example.identity.dto.response.AddressResponse;
import org.example.identity.repository.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    //create
    AddressResponse createAddress(Long id ,AddressCreateRequest addressCreateRequest);
    //read
    AddressResponse getById(Long addressId);

    //update
    AddressResponse updateAddress(Long addressId,AddressUpdateRequest addressUpdateRequest);
    boolean restoreById(Long addressId);
    //delete
    boolean softDeleteById(Long addressId);
    //list
    Page<AddressResponse> listByUser(Long userId, Pageable pageable);
    //default address
    boolean setDefault(Long userId,Long address);
    AddressResponse getDefault(Long userId);


}
