package org.example.identity.service;


import org.example.identity.dto.request.UserCreateRequest;
import org.example.identity.dto.request.UserUpdateRequest;
import org.example.identity.dto.response.RoleResponse;
import org.example.identity.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    //Create & Update
    UserResponse createUser(UserCreateRequest userCreateRequest);
    UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest);

    // Read
    UserResponse findById(Long id);
    UserResponse findByEmail(String email);
    UserResponse findByPhone(String phone);
    // Exists
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    // List & search
    Page<UserResponse> listActiveUsers(Pageable pageable);
    Page<UserResponse> listDeletedUsers(Pageable pageable);
    Page<UserResponse> searchUsers(String keyword, Pageable pageable);
    // Delete
    boolean softDeleteById(Long id);

}
