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
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final AntPathMatcher PATH = new AntPathMatcher();
    private static final List<String> WHITELIST = List.of(
            "/",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger",
            "/auth/**",
            "/h2-console/**"
    );

    private final JwtTokenProvider tokenProvider;
    private final UsersRepository usersRepository;



    public JwtAuthFilter(JwtTokenProvider tokenProvider, UsersRepository usersRepository) {
        this.tokenProvider = tokenProvider;
        this.usersRepository = usersRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        String uri = req.getRequestURI();
        for (String p : WHITELIST) {
            if (PATH.match(p, uri)) {
                chain.doFilter(req, res);
                return;
            }
        }

        String header = req.getHeader("Authorization");
        logger.debug(req.getMethod() + " " + req.getRequestURI() + " | Authorization=" + header);
        if (!StringUtils.hasText(header)) {
            logger.debug("JWT: Authorization header missing");
        } else {
            logger.debug("JWT: Authorization header = " + header);
        }
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String access = header.substring(7);
            try {
                Jws<Claims> jws = tokenProvider.parse(access);
                Integer userId = jws.getBody().get("userId", Integer.class);
                String loginId = jws.getBody().getSubject();
                logger.debug("JWT parsed OK: sub=" + loginId + ", userId=" + userId);

                Optional<Users> usersOptional = usersRepository.findById(userId);
                if (usersOptional.isPresent()&&usersOptional.get().getDeletedAt() == null){
                    Users u = usersOptional.get();
                    CustomUserDetails userDetails = new CustomUserDetails(
                            u.getUserId(),
                            u.getLoginId(),
                            u.getPasswordHash(),
                            Collections.emptyList()
                    );
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.debug("Authentication set for user: "+loginId);
//                Users u = usersRepository.findById(userId).orElse(null);
//                if (u != null && u.getDeletedAt() == null) {
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(u, null, List.of());
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    logger.debug("JWT user not found or deleted");
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                logger.debug("JWT parse/validate failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(req, res);
    }
}
