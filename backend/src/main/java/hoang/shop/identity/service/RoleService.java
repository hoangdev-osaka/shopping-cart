package hoang.shop.identity.service;


import hoang.shop.common.enums.status.RoleStatus;
import hoang.shop.identity.dto.request.RoleCreateRequest;
import hoang.shop.identity.dto.request.RoleUpdateRequest;
import hoang.shop.identity.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleResponse create(RoleCreateRequest roleCreateRequest);
    RoleResponse findById(Long id);
    RoleResponse findByName(String name);
    Page<RoleResponse> findAllActive(Pageable pageable);
    Page<RoleResponse> findAllDeleted(Pageable pageable);
    Page<RoleResponse> search(String keyword, RoleStatus status, Pageable pageable);
    RoleResponse update(Long id, RoleUpdateRequest roleUpdateRequest);
    void softDeleteById(Long id);
    void restoreById(Long id);
    boolean existsByName(String name);

}
