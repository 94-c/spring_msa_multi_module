package com.backend.gateway.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String username;
    private final String role;

    public JwtAuthentication(String username, String role) {
        super(Collections.singletonList(new SimpleGrantedAuthority(role)));
        this.username = username;
        this.role = role;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // 토큰 기반 인증에서는 별도의 자격 증명이 필요 없음
    }

    @Override
    public Object getPrincipal() {
        return username; // 인증된 사용자 이름 반환
    }
}
