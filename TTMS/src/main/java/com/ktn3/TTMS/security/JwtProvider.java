package com.ktn3.TTMS.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // Sinh access token
    public String generateAccessToken(Long userId, String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email) // sub: Email của user (mặc định là Subject)
                .claim("userId", userId) // Thêm 1 claim tuỳ chỉnh: userId
                .claim("roles", roles)   // Thêm 1 claim tuỳ chỉnh: danh sách role
                .setIssuedAt(new Date()) // Thời điểm phát hành token (iat)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Hạn dùng (exp)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512) // Ký token bằng secret
                .compact();
    }

    // Parse claims
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check hợp lệ
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // Lấy email từ token
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Phương thức lấy email từ Authentication
    public static String getAuthenticatedUserEmail() {
        // Lấy Authentication từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Lấy principal (UserDetails) từ Authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return userDetails.getUsername(); // Email được lưu trong username
        }

        // Trường hợp không có authentication hoặc không có UserDetails
        return "anonymous";
    }
}

