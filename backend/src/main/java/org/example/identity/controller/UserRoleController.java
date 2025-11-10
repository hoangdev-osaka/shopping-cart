package org.example.identity.controller;


import lombok.RequiredArgsConstructor;
import org.example.identity.dto.response.RoleResponse;
import org.example.identity.dto.response.UserResponse;
import org.example.identity.dto.response.UserRoleResponse;
import org.example.identity.model.UserRole;
import org.example.identity.service.UserRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/user-role")
@RequiredArgsConstructor
public class UserRoleController {
    private final UserRoleService userRoleService;

    @PostMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Boolean> assignRoleToUser(
            @PathVariable Long userId,
            @PathVariable Long roleId,
            @RequestHeader("X-Actor-Id") Long actorId) {
        boolean created = userRoleService.assignRoleToUser(userId,roleId,actorId);
        if (created){
            return ResponseEntity
                    .created(URI.create("/api/users/%d/roles/%d".formatted(userId, roleId))).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId,
            @RequestHeader("X-Actor-Id") Long actorId) {
        boolean removed = userRoleService.removeRoleFromUser(userId, roleId, actorId);
        return removed ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<RoleResponse>> listRolesOfUser(
            @PathVariable Long userId,
            @PageableDefault(page = 0,size = 20,sort = "id",direction = Sort.Direction.DESC )Pageable pageable) {
        Page<RoleResponse> page = userRoleService.listRolesOfUser(userId,pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/roles/{roleId}")
    public ResponseEntity<Page<UserResponse>> listUserOfRole(
            @PathVariable Long roleId,
            @PageableDefault(page = 0,size = 20,sort = "id",direction = Sort.Direction.DESC ) Pageable pageable) {
        Page<UserResponse> page = userRoleService.listUserOfRole(roleId, pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/users/{userId}/roles/{roleId}/is")
    public ResponseEntity<Boolean> userHasRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        boolean hasRole = userRoleService.userHasRole(userId, roleId);
        return ResponseEntity.ok(hasRole);
    }
    @GetMapping
    public ResponseEntity<Page<UserRoleResponse>> listUserRole(
            @PageableDefault(
                    page = 0,size = 20,sort = {"assignedAt","userId"},
                    direction = Sort.Direction.DESC)
            Pageable pageable){
        Page<UserRoleResponse> userRoleResponses = userRoleService.listUserRole(pageable);
        return ResponseEntity.ok(userRoleResponses);
    }
}
