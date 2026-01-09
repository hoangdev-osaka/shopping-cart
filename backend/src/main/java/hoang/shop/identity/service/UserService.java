package hoang.shop.identity.service;


import hoang.shop.identity.dto.request.RegisterRequest;
import hoang.shop.identity.dto.request.UserUpdateRequest;
import hoang.shop.identity.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse findById(Long id);
    Page<UserResponse> searchUsers(String keyword, Pageable pageable);
    // Delete
    void markBanned(Long id);
    void markDeleted(Long id);
    void restoreACTIVE(Long id);
}
