package com.mobile.banking.Customer.filter;

import com.mobile.banking.Customer.exception.InvalidTokenException;
import com.mobile.banking.Customer.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    private final List<AntPathRequestMatcher> excludedMatchers;

    public JwtAuthenticationFilter (List<AntPathRequestMatcher> excludedMatchers) {
        excludedMatchers.add(AntPathRequestMatcher.antMatcher("/authenticate"));
        excludedMatchers.add(AntPathRequestMatcher.antMatcher("/swagger-ui"));
        excludedMatchers.add(AntPathRequestMatcher.antMatcher("/h2-console/"));
        this.excludedMatchers = excludedMatchers;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String customerId = null;
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                customerId = jwtService.extractCustomerId(token);

                jwtService.validateToken(token);
            } else {
                throw new InvalidTokenException("Please provide authorization token");

            }
            request.setAttribute("customerId", customerId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludedMatchers.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }
}
