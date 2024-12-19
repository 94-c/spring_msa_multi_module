package com.backend.gateway.filter;

import com.backend.gateway.util.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationWebFilter createJwtAuthenticationFilter(JwtAuthenticationManager authenticationManager) {
        AuthenticationWebFilter jwtAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);

        jwtAuthenticationFilter.setServerAuthenticationConverter(exchange -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtTokenProvider.validateToken(token)) {
                    Claims claims = jwtTokenProvider.getClaims(token);
                    String username = claims.getSubject();
                    String role = (String) claims.get("role");

                    return Mono.just(new JwtAuthenticationToken(username, role));
                }
            }
            return Mono.empty();
        });
        return jwtAuthenticationFilter;
    }
}
