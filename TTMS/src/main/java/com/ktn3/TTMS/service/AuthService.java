package com.ktn3.TTMS.service;

import com.ktn3.TTMS.dto.request.auth.ReqLogin;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.RefreshToken;
import com.ktn3.TTMS.entity.User;

public interface AuthService {
    ResCommonApi<?>  verifyAccount(String token);
    ResCommonApi<?>  login(ReqLogin req);
    ResCommonApi<?> forgotPassword(String email);
    ResCommonApi<?> resetPassword(String token, String newPassword);

    RefreshToken createRefreshToken(User user);
    boolean isValidRefreshToken(String token);
    void deleteRefreshToken(String token);
    ResCommonApi<?> refreshToken(String refreshToken);
}
