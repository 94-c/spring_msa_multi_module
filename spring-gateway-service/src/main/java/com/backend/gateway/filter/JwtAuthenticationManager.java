package com.backend.gateway.filter;

import com.backend.gateway.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        if (jwtTokenProvider.validateToken(token)) {
            var claims = jwtTokenProvider.getClaims(token);

            // 사용자 정보와 권한 생성
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            var userDetails = new User(username, "", authorities);

            var auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities
            );

            SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContext에 인증 정보 저장
            return Mono.just(auth);
        }

        return Mono.empty(); // 인증 실패 시
    }
}
