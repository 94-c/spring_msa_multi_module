package com.backend.gateway.config;

import com.backend.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource; // GlobalCorsConfig의 Bean 주입

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // HTTP Basic 비활성화
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // 기본 Form Login 비활성화
                //.csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // CORS 설정
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**", "/user-service/**", "/admin-service/**", "/actuator/**").permitAll()
                        .pathMatchers("/users/**").hasRole("USER")
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter.createJwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION) // JWT 필터 추가
                .build();
    }
}
