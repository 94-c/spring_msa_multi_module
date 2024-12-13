package com.backend.core.auth.service;

import com.backend.core.auth.exception.EmailAlreadyExistsException;
import com.backend.core.auth.exception.InvalidPasswordException;
import com.backend.core.auth.exception.RoleNotFoundException;
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

    public void register(AuthRegisterRequest request) {
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
    }
}

