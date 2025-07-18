package com.ktn3.TTMS.controller;

import com.ktn3.TTMS.dto.request.user.ReqCreateUser;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResCommonApi<?>> register(@Valid @RequestBody ReqCreateUser req) {
        ResCommonApi<?> response = userService.create(req);

        // Nếu muốn tự động status code (dựa trên trường statusCode trong ResCommonApi)
        // hoặc trả về mặc định 200 nếu statusCode bị null
        // gần như sẽ không có null vì: success thì tự động gán status, error thì gán thủ côngở service
        int status = response.getStatusCode() != null ? response.getStatusCode() : 200;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ResCommonApi<?>> getUserById(@PathVariable Long id) {
        ResCommonApi<?> response = userService.findById(id);

        // Lấy statusCode từ response, nếu không có thì mặc định 200
        // gần như sẽ không có null vì: success thì tự động gán status, error thì gán thủ côngở service
        int status = response.getStatusCode() != null ? response.getStatusCode() : 200;
        return ResponseEntity.status(status).body(response);
    }
}
