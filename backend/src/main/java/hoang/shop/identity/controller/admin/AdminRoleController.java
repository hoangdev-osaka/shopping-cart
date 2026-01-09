package hoang.shop.identity.controller.admin;

import hoang.shop.common.enums.status.RoleStatus;
import hoang.shop.identity.dto.request.RoleCreateRequest;
import hoang.shop.identity.dto.request.RoleUpdateRequest;
import hoang.shop.identity.dto.response.RoleResponse;
import hoang.shop.identity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class AdminRoleController {
    private final RoleService roleService;
    @PostMapping
    public ResponseEntity<RoleResponse> create(@RequestBody RoleCreateRequest roleCreateRequest) {
        return ResponseEntity.ok(roleService.create(roleCreateRequest));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }
    @GetMapping
    public ResponseEntity<Page<RoleResponse>> search(@RequestParam("keyword") String keyword,@RequestParam("status") RoleStatus status, Pageable pageable) {
        return ResponseEntity.ok(roleService.search(keyword,status, pageable));
    }
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable Long id, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return ResponseEntity.ok(roleService.update(id, roleUpdateRequest));
    }
    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> softDeleteById(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreById(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}

