package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.dto.request.auth.ReqLogin;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.dto.response.auth.ResLogin;
import com.ktn3.TTMS.entity.RefreshToken;
import com.ktn3.TTMS.entity.Role;
import com.ktn3.TTMS.entity.User;
import com.ktn3.TTMS.repository.RefreshTokenRepo;
import com.ktn3.TTMS.repository.UserRepo;
import com.ktn3.TTMS.security.JwtProvider;
import com.ktn3.TTMS.service.AuthService;
import com.ktn3.TTMS.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepo refreshTokenRepo;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Override
    public ResCommonApi<?> verifyAccount(String token) {
        Optional<User> userOpt = userRepo.findByEmailVerificationToken(token);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(true);
            user.setEmailVerificationToken(null);
            user.setEmailVerifiedAt(LocalDateTime.now());
            userRepo.save(user);

            return ResCommonApi.success(null, "Xác thực thành công! Bạn có thể đăng nhập.");
        } else {
            return ResCommonApi.error("Token không hợp lệ hoặc đã hết hạn.", 400);
        }
    }

    @Override
    public ResCommonApi<?> login(ReqLogin req) {
        // 1. Tìm user theo email
        Optional<User> userOpt = userRepo.findByEmail(req.getEmail());
        if (userOpt.isEmpty()) {
            return ResCommonApi.error("Sai email hoặc mật khẩu", 401);
        }
        User user = userOpt.get();

        // 2. Kiểm tra đã active hay chưa
        if (!user.isActive()) {
            return ResCommonApi.error("Tài khoản chưa được xác thực email hoặc đang tạm khóa!", 403);
        }

        // 3. Kiểm tra mật khẩu
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResCommonApi.error("Sai email hoặc mật khẩu", 401);
        }

        // 4. Đã active và mật khẩu đúng, sinh JWT token
        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRoles().stream().map(Role::getName).toList());
        RefreshToken refreshToken = createRefreshToken(user);

        // 5. Trả về info + token
        ResLogin resLogin = ResLogin.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();

        return ResCommonApi.success(resLogin, "Đăng nhập thành công!");
    }

    @Override
    public ResCommonApi<?> forgotPassword(String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Vẫn trả về message thành công, không tiết lộ email tồn tại
            return ResCommonApi.success(null, "Nếu email tồn tại, hệ thống đã gửi hướng dẫn reset mật khẩu.");
        }
        User user = userOpt.get();

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpired(LocalDateTime.now().plusMinutes(30)); // token sống 30 phút
        userRepo.save(user);

        // Gửi mail
        mailService.sendResetPasswordEmail(user.getEmail(), token);

        return ResCommonApi.success(null, "Nếu email tồn tại, hệ thống đã gửi hướng dẫn reset mật khẩu.");
    }

    @Override
    public ResCommonApi<?> resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepo.findByResetPasswordToken(token);
        if (userOpt.isEmpty()) {
            return ResCommonApi.error("Token không hợp lệ hoặc đã hết hạn!", 400);
        }
        User user = userOpt.get();

        if (user.getResetPasswordTokenExpired() == null || user.getResetPasswordTokenExpired().isBefore(LocalDateTime.now())) {
            return ResCommonApi.error("Token đã hết hạn!", 400);
        }

        // Đổi mật khẩu và xóa token reset
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpired(null);
        userRepo.save(user);

        return ResCommonApi.success(null, "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập.");
    }

    // Tạo refresh token, lưu DB
    @Override
    public RefreshToken createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(expiredAt)
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    // Kiểm tra refresh token hợp lệ
    @Override
    public boolean isValidRefreshToken(String token) {
        return refreshTokenRepo.findByToken(token)
                .filter(rt -> rt.getExpiredAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    // Xóa refresh token khi logout hoặc hết hạn
    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepo.findByToken(token)
                .ifPresent(refreshTokenRepo::delete);
    }

    @Override
    public ResCommonApi<?> refreshToken(String refreshToken) {
        Optional<RefreshToken> opt = refreshTokenRepo.findByToken(refreshToken);

        if (opt.isEmpty() || opt.get().getExpiredAt().isBefore(LocalDateTime.now())) {
            return ResCommonApi.error("Refresh token không hợp lệ hoặc đã hết hạn", 401);
        }

        User user = opt.get().getUser();
        String newAccessToken = jwtProvider.generateAccessToken(
                user.getId(), user.getEmail(),
                user.getRoles().stream().map(Role::getName).toList()
        );

        Map<String, Object> data = new HashMap<>();
        data.put("accessToken", newAccessToken);
        data.put("refreshToken", refreshToken); // hoặc tạo refresh mới nếu muốn

        return ResCommonApi.success(data, "Làm mới token thành công!");
    }

}
