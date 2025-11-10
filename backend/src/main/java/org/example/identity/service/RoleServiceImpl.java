package org.example.identity.service;

import lombok.RequiredArgsConstructor;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.NotFoundException;
import org.example.identity.dto.request.RoleCreateRequest;
import org.example.identity.dto.request.RoleUpdateRequest;
import org.example.identity.dto.response.RoleResponse;
import org.example.identity.mapper.RoleMapper;
import org.example.identity.model.Role;
import org.example.identity.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleCreateRequest roleCreateRequest) {
        if (roleRepository.existsByNameIgnoreCase(roleCreateRequest.name()))
            throw new AlreadyExistsException("{error.role.name.duplicate}");
        Role role = roleMapper.toEntity(roleCreateRequest);
        return roleMapper.toResponse(roleRepository.save(role));
    }
    // tìm role bằng id
    @Override
    public RoleResponse findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("error.role.id.notFound"));
        return roleMapper.toResponse(role);
    }
    // tìm role bằng tên quyền
    @Override
    public RoleResponse findByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(()-> new NotFoundException("error.role.name.notFound"));
        return roleMapper.toResponse(role);
    }
    // tìm list role active
    @Override
    public Page<RoleResponse> findAllActive(Pageable pageable) {
        return roleRepository.findAllByDeletedFalse(pageable).map(roleMapper::toResponse);
    }
    // tìm list role deleted
    @Override
    public Page<RoleResponse> findAllDeleted(Pageable pageable) {
        return roleRepository.findAllByDeletedTrue(pageable).map(roleMapper::toResponse);
    }
    // tìm kiếm role bằng từ khóa
    @Override
    public Page<RoleResponse> search(String keyword, Pageable pageable) {
        return roleRepository.searchByKeyword(keyword,pageable).map(roleMapper::toResponse);
    }
    // cập nhật role bằng id
    @Override
    public RoleResponse update(Long id, RoleUpdateRequest roleUpdateRequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("error.role.id.notFound"));
        roleMapper.updateEntityFromDto(roleUpdateRequest,role);
        return roleMapper.toResponse(role);
    }
    // xóa mềm role bằng id
    @Override
    public boolean softDeleteById(Long id) {
        return roleRepository.softDeleteById(id)>0;
    }
    // khôi phục role đã xóa bằng id
    @Override
    public boolean restoreById(Long id) {
        return roleRepository.softDeleteById(id)>0;
    }
    // kiểm tra tên của role có tồn tại hay không (cả hoa cả thường)
    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByNameIgnoreCase(name);
    }
}
