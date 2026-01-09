package hoang.shop.identity.service;

import hoang.shop.common.enums.status.UserStatus;
import lombok.RequiredArgsConstructor;
import hoang.shop.common.exception.NotFoundException;
import hoang.shop.identity.dto.response.UserResponse;
import hoang.shop.identity.mapper.UserMapper;
import hoang.shop.identity.model.User;
import hoang.shop.identity.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("{error.user.id.notFound}"));
        return userMapper.toResponse(user);
    }
    @Override
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchByKeyword(keyword, pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public void markBanned(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("{error.user.id.notFound}"));
        user.setStatus(UserStatus.BANNED);
    }

    @Override
    public void markDeleted(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("{error.user.id.notFound}"));
        user.setStatus(UserStatus.DELETED);

    }
    @Override
    public void restoreACTIVE(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("{error.user.id.notFound}"));
        user.setStatus(UserStatus.ACTIVE);
    }
}

