package com.ktn3.TTMS.controller;

import com.ktn3.TTMS.dto.request.auth.ReqForgotPassword;
import com.ktn3.TTMS.dto.request.auth.ReqLogin;
import com.ktn3.TTMS.dto.request.auth.ReqResetPassword;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.RefreshToken;
import com.ktn3.TTMS.entity.Role;
import com.ktn3.TTMS.entity.User;
import com.ktn3.TTMS.repository.RefreshTokenRepo;
import com.ktn3.TTMS.security.JwtProvider;
import com.ktn3.TTMS.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepo refreshTokenRepo;

    @GetMapping("/verify")
    public ResponseEntity<ResCommonApi<?>> verifyAccount(@RequestParam("token") String token) {
        ResCommonApi<?> response = authService.verifyAccount(token);

        // Lấy statusCode từ response, nếu không có thì mặc định 200
        // gần như sẽ không có null vì: success thì tự động gán status, error thì gán thủ công ở service
        int status = response.getStatusCode() != null ? response.getStatusCode() : 200;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResCommonApi<?>> login(@Valid @RequestBody ReqLogin req){
        ResCommonApi<?> response = authService.login(req);

        // Lấy statusCode từ response, nếu không có thì mặc định 200
        // gần như sẽ không có null vì: success thì tự động gán status, error thì gán thủ công ở service
        int status = response.getStatusCode() != null ? response.getStatusCode() : 200;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResCommonApi<?>> forgotPassword(@RequestBody @Valid ReqForgotPassword req) {
        return ResponseEntity.ok(authService.forgotPassword(req.getEmail()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResCommonApi<?>> resetPassword(@RequestBody @Valid ReqResetPassword req) {
        return ResponseEntity.ok(authService.resetPassword(req.getToken(), req.getNewPassword()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResCommonApi<?>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        ResCommonApi<?> response = authService.refreshToken(refreshToken);
        int status = response.getStatusCode() != null ? response.getStatusCode() : 200;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResCommonApi<?>> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        authService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok(ResCommonApi.success(null, "Đăng xuất thành công!"));
    }



}
