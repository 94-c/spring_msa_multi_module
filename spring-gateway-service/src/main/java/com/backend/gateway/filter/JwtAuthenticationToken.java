package com.backend.gateway.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String principal;

    public JwtAuthenticationToken(String username, String role) {
        super(Collections.singletonList(new SimpleGrantedAuthority(role)));
        this.principal = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // 자격 증명은 필요하지 않음
    }

    @Override
    public Object getPrincipal() {
        return principal; // 사용자 이름 반환
    }
}
