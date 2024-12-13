package com.backend.gateway.filter;

import com.backend.gateway.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtAuthenticationWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // Authorization 헤더가 없는 경우
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            // JWT 토큰 추출
            String token = authHeader.replace("Bearer ", "");
            Claims claims = JwtUtil.validateTokenAndGetClaims(token);

            // 사용자 역할 검증
            String roles = claims.get("roles", String.class);
            if (roles == null || (!roles.contains("USER") && !roles.contains("ADMIN"))) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // 요청을 다음 필터 체인으로 전달
            return chain.filter(exchange);
        } catch (Exception e) {
            // JWT 유효성 검증 실패 시
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
