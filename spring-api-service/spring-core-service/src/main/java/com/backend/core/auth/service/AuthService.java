package com.backend.core.auth.service;

import com.backend.core.auth.dto.AuthLoginRequest;
import com.backend.core.auth.exception.UserNotFoundException;
import com.backend.core.auth.exception.EmailAlreadyExistsException;
import com.backend.core.auth.exception.InvalidPasswordException;
import com.backend.core.auth.exception.RoleNotFoundException;
import com.backend.core.common.jwt.JwtTokenProvider;
import com.backend.core.entity.role.Role;
import com.backend.core.entity.role.RoleName;
import com.backend.core.entity.role.RoleRepository;
import com.backend.core.entity.user.User;
import com.backend.core.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.backend.core.auth.dto.AuthRegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User register(AuthRegisterRequest request) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // 비밀번호 검증
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new InvalidPasswordException("Password cannot be null or empty");
        }

        // 기본 역할 조회
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Default role not found"));

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encryptedPassword);
        user.addRole(userRole);

        // 사용자 저장
        userRepository.save(user);

        return user;
    }

    public String login(AuthLoginRequest request) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid email or password");
        }

        // 사용자 역할 조회 및 문자열로 변환
        String role = user.getRoles().stream()
                .map(Role::getName)
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("User role not found"))
                .name(); // 문자열로 변환

        // JWT 생성 및 반환
        return jwtTokenProvider.createToken(user.getEmail(), role);
    }
}

