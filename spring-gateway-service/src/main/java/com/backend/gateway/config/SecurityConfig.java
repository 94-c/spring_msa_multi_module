package com.backend.gateway.config;

import com.backend.gateway.filter.JwtAuthenticationFilter;
import com.backend.gateway.filter.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationManager authenticationManager;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF 비활성화
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**", "/user-service/**", "/admin-service/**").permitAll() // 공개 엔드포인트
                        .pathMatchers("/users/**").hasRole("USER") // USER 권한 필요
                        .pathMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한 필요
                        .anyExchange().authenticated() // 나머지 요청은 인증 필요
                )
                .addFilterAt(jwtAuthenticationFilter.createJwtAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION); // JWT 필터 추가

        return http.build();
    }
}
