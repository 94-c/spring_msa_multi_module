package com.backend.core.auth.controller;


import com.backend.core.auth.dto.AuthLoginRequest;
import com.backend.core.common.response.ApiResponse;
import com.backend.core.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.backend.core.auth.dto.AuthRegisterRequest;
import com.backend.core.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 요청
     * @param request 회원가입 요청 데이터 (email, password)
     * @return 성공 메시지
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody AuthRegisterRequest request) {
        User result = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(result));
    }

    /**
     * 로그인 요청
     * @param request 로그인 요청 데이터 (email, password)
     * @return JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthLoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(token)); // JWT 토큰 반환
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth Service is running");
    }
}
