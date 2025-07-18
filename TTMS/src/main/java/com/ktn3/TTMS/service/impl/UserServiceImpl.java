package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.dto.request.user.ReqCreateUser;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.dto.response.user.ResUser;
import com.ktn3.TTMS.entity.User;
import com.ktn3.TTMS.repository.UserRepo;
import com.ktn3.TTMS.service.MailService;
import com.ktn3.TTMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResCommonApi<?> create(ReqCreateUser req) {
        try {
            User user = User.builder()
                    .email(req.getEmail())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .avatarUrl(req.getAvatarUrl())
                    .name(req.getName())
                    .phone(req.getPhone())
                    .gender(req.getGender())
                    .active(false) // mặc định chưa active khi mới tạo
                    .emailVerificationToken(UUID.randomUUID().toString()) // sinh token xác thực email
                    .build();

            User saveUser = userRepo.save(user);

            // send email verify account
            mailService.sendVerificationEmail(saveUser);

            return ResCommonApi.success(null, "Create user success. Please check your email to activate your account.");
        }catch (Exception e){
            return ResCommonApi.error("Create user error: " + e.getMessage(), 500);
        }
    }

    @Override
    public ResCommonApi<?> findById(Long id) {
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("User with id: " + id + " is null"));

            if (user.isActive()) {
                ResUser res = ResUser.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .avatarUrl(user.getAvatarUrl())
                        .phone(user.getPhone())
                        .gender(user.getGender())
                        .build();

                return ResCommonApi.success(res, "Find user by id success!");
            } else {
                return ResCommonApi.success(null, "Account is disabled!");
            }
        } catch (Exception e) {
            return ResCommonApi.error("Find user by id error: " + e.getMessage(), 500);
        }
    }


}
