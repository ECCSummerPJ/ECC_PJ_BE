package com.linkrap.BE.security;

import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UsersRepository usersRepository;

    public JwtAuthFilter(JwtTokenProvider tokenProvider, UsersRepository usersRepository) {
        this.tokenProvider = tokenProvider;
        this.usersRepository = usersRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String access = header.substring(7);
            try {
                Jws<Claims> jws = tokenProvider.parse(access);
                Integer userId = jws.getBody().get("userId", Integer.class);
                String loginId = jws.getBody().getSubject();

                //DB 상태 확인
                Users u = usersRepository.findById(userId).orElse(null);
                if (u != null && u.getDeletedAt() == null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(u, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // 토큰이 유효하지 않으면 인증 컨텍스트 비움 (요청은 계속 진행 -> 401은 엔드포인트가 보호되어 있으면 발생)
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(req, res);
    }
}
