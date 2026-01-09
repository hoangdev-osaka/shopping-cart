package hoang.shop.identity.service;

import hoang.shop.common.enums.status.RoleStatus;
import lombok.RequiredArgsConstructor;
import hoang.shop.common.exception.AlreadyExistsException;
import hoang.shop.common.exception.NotFoundException;
import hoang.shop.identity.dto.request.RoleCreateRequest;
import hoang.shop.identity.dto.request.RoleUpdateRequest;
import hoang.shop.identity.dto.response.RoleResponse;
import hoang.shop.identity.mapper.RoleMapper;
import hoang.shop.identity.model.Role;
import hoang.shop.identity.repository.RoleRepository;
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
    @Override
    public RoleResponse findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("{error.role.id.not-found}"));
        return roleMapper.toResponse(role);
    }
    @Override
    public RoleResponse findByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(()-> new NotFoundException("{error.role.name.not-found}"));
        return roleMapper.toResponse(role);
    }
    @Override
    public Page<RoleResponse> findAllActive(Pageable pageable) {
        return roleRepository.findAllByStatus(RoleStatus.ACTIVE,pageable).map(roleMapper::toResponse);
    }
    @Override
    public Page<RoleResponse> findAllDeleted(Pageable pageable) {
        return roleRepository.findAllByStatus(RoleStatus.INACTIVE,pageable).map(roleMapper::toResponse);
    }
    @Override
    public Page<RoleResponse> search(String keyword,RoleStatus status, Pageable pageable) {
        return roleRepository.searchByKeyword(keyword,status,pageable).map(roleMapper::toResponse);
    }
    @Override
    public RoleResponse update(Long id, RoleUpdateRequest roleUpdateRequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("{error.role.id.not-found}"));
        roleMapper.updateEntityFromDto(roleUpdateRequest,role);
        return roleMapper.toResponse(role);
    }
    @Override
    public void softDeleteById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()->new NotFoundException("{error.role.id.not-found}"));
    }
    @Override
    public void restoreById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(()->new NotFoundException("{error.role.id.not-found}"));
    }
    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByNameIgnoreCase(name);
    }
}
