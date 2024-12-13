package com.backend.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .authorizeExchange()
                .pathMatchers("/users/**").hasRole("USER") // USER 권한 필요
                .pathMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한 필요
                .pathMatchers("/user-service/**", "/admin-service/**").permitAll() // Actuator 엔드포인트 허용
                .anyExchange().authenticated()
                .and()
                .addFilterAt(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION); // JWT 필터 추가

        return http.build();
    }

    private AuthenticationWebFilter jwtAuthenticationWebFilter() {
        ReactiveAuthenticationManager authenticationManager = authentication -> {
            // JWT 검증 로직 구현 필요
            return Mono.just(authentication);
        };

        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager);
        filter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/users/**", "/admin/**"));
        filter.setSecurityContextRepository(new CustomSecurityContextRepository()); // SecurityContext 저장소 설정
        return filter;
    }

    private static class CustomSecurityContextRepository implements ServerSecurityContextRepository {
        @Override
        public Mono<Void> save(ServerWebExchange exchange, org.springframework.security.core.context.SecurityContext context) {
            // SecurityContext 저장 로직 구현 필요
            return Mono.empty();
        }

        @Override
        public Mono<org.springframework.security.core.context.SecurityContext> load(ServerWebExchange exchange) {
            // SecurityContext 로드 로직 구현 필요
            return Mono.empty();
        }
    }
}
