package hoang.shop.identity.service;

import hoang.shop.identity.dto.request.*;
import hoang.shop.identity.dto.response.MyAccountResponse;
import hoang.shop.identity.dto.response.SessionInfoResponse;
import hoang.shop.identity.dto.response.UserResponse;

import java.util.List;

public interface MyAccountService {
    MyAccountResponse getMyAccount();

    UserResponse updateMyAccount(Long id, UserUpdateRequest userUpdateRequest);

    void changePassword(ChangePasswordRequest request);

    void changeEmail(ChangeEmailRequest request);

    void deleteAccount(DeleteAccountRequest request);

    List<SessionInfoResponse> getSessions();

    void revokeSession(Long sessionId);

    void revokeAllSessions();


}
