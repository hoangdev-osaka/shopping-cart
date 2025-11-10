package org.example.identity.service;


import org.example.identity.dto.request.RoleCreateRequest;
import org.example.identity.dto.request.RoleUpdateRequest;
import org.example.identity.dto.response.RoleResponse;
import org.example.identity.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    //create
    RoleResponse create(RoleCreateRequest roleCreateRequest);
    //read
    RoleResponse findById(Long id);
    RoleResponse findByName(String name);
    Page<RoleResponse> findAllActive(Pageable pageable);
    Page<RoleResponse> findAllDeleted(Pageable pageable);
    Page<RoleResponse> search(String keyword,Pageable pageable);
    //update
    RoleResponse update(Long id, RoleUpdateRequest roleUpdateRequest);
    //delete
    boolean softDeleteById(Long id);
    boolean restoreById(Long id);
    boolean existsByName(String name);

}
