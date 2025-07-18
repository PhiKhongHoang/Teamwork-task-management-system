package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmailVerificationToken(String token);

    Optional<User> findByEmail(@Email @NotBlank String email);

    Optional<User> findByResetPasswordToken(String token);
}
