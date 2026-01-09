package hoang.shop.identity.controller.admin;

import hoang.shop.identity.dto.request.UserUpdateRequest;
import hoang.shop.identity.dto.response.UserResponse;
import hoang.shop.identity.service.UserRoleService;
import hoang.shop.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserRoleService userRoleService;
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @RequestParam(defaultValue = "") String keyword,
            @PageableDefault(
                    page = 0,
                    size = 15,
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsers(keyword, pageable));
    }
    @GetMapping("/roles/{roleId}")
    public ResponseEntity<Page<UserResponse>> listUserOfRole(
            @PathVariable Long roleId,
            @PageableDefault(page = 0,size = 20,sort = "id",direction = Sort.Direction.DESC ) Pageable pageable) {
        Page<UserResponse> page = userRoleService.listUserOfRole(roleId, pageable);
        return ResponseEntity.ok(page);
    }
    @PatchMapping("/{userId}/mark-banned")
    public void markBanned(@PathVariable Long userId) {
        userService.markBanned(userId);
    }
    @PatchMapping("/{userId}/mark-deleted")
    public void markDeleted(@PathVariable Long userId) {
        userService.markDeleted(userId);
    }
    @PatchMapping("/{userId}/mark-active")
    public void restoreACTIVE(@PathVariable Long userId) {
        userService.restoreACTIVE(userId);
    }
}
