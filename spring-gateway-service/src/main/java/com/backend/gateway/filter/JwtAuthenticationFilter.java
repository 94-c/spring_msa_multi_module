package com.backend.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter {
    private final JwtAuthenticationManager authenticationManager;

    // 예외 처리할 경로 리스트
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/auth/",
            "/user-service/",
            "/admin-service/"
    );

    public AuthenticationWebFilter createJwtAuthenticationFilter() {
        AuthenticationWebFilter jwtAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);

        jwtAuthenticationFilter.setServerAuthenticationConverter(new ServerAuthenticationConverter() {
            @Override
            public Mono<Authentication> convert(ServerWebExchange exchange) {
                String path = exchange.getRequest().getPath().value();

                // 예외 경로 확인
                if (isExcludedPath(path)) {
                    return Mono.empty(); // 예외 경로는 필터를 건너뜀
                }

                // Authorization 헤더에서 토큰 추출
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    return Mono.just(new UsernamePasswordAuthenticationToken(null, token));
                }

                // 인증 정보가 없는 경우
                return Mono.empty();
            }
        });

        return jwtAuthenticationFilter;
    }

    // 요청 경로가 예외 처리 리스트에 포함되는지 확인
    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
}
