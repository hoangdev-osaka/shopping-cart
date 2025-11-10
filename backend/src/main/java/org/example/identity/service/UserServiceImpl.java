package org.example.identity.service;

import lombok.RequiredArgsConstructor;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.BadRequestException;
import org.example.common.exception.DuplicateResourceException;
import org.example.common.exception.NotFoundException;
import org.example.identity.dto.request.UserCreateRequest;
import org.example.identity.dto.request.UserUpdateRequest;
import org.example.identity.dto.response.RoleResponse;
import org.example.identity.dto.response.UserResponse;
import org.example.identity.mapper.RoleMapper;
import org.example.identity.mapper.UserMapper;
import org.example.identity.model.User;
import org.example.identity.model.UserRole;
import org.example.identity.repository.RoleRepository;
import org.example.identity.repository.UserRepository;
import org.example.identity.repository.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // create
    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByEmail(userCreateRequest.email()))
            throw new DuplicateResourceException("{error.user.email.exists}");
        User user = userMapper.toEntity(userCreateRequest);
        user = userRepository.saveAndFlush(user);
        return userMapper.toResponse(user);
    }

    // update
    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("{error.user.id.notFound}"));
        userMapper.updateEntityFromDto(userUpdateRequest, user);
        return userMapper.toResponse(userRepository.save(user));
    }


    // read (find)
    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("{error.user.id.notFound}"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("{error.user.email.notFound}"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findByPhone(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("{error.user.phone.notFound}"));
        return userMapper.toResponse(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> listActiveUsers(Pageable pageable) {
        return userRepository.findByDeletedFalse(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public Page<UserResponse> listDeletedUsers(Pageable pageable) {
        return userRepository.findByDeletedTrue(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchByKeyword(keyword, pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    // delete
    @Override
    public boolean softDeleteById(Long id) {
        return userRepository.softDeleteById(id) > 0;
    }
}

