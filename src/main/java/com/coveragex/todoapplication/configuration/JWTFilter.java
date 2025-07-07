package com.coveragex.todoapplication.configuration;


import com.coveragex.todoapplication.entity.User;
import com.coveragex.todoapplication.service.UserService;
import com.coveragex.todoapplication.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    public JWTFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        logger.info("Processing request for path: {}", path);

        boolean isPermitted = false;
        for (String permittedPath : SecurityConstants.PERMITTED_PATHS) {
            String cleanPermittedPath = permittedPath.replace("/**", "");
            if (permittedPath.endsWith("/**")) {
                if (path.startsWith(cleanPermittedPath)) {
                    isPermitted = true;
                    break;
                }
            } else {
                if (path.equals(permittedPath)) {
                    isPermitted = true;
                    break;
                }
            }
        }

        if (isPermitted) {
            logger.info("Skipping authentication for permitted path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.info("Found cookie: {}", cookie.getName());
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    logger.info("Found access token");
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    logger.info("Found refresh token");
                }
            }
        } else {
            logger.info("No cookies found in request");
        }

        if (accessToken == null && refreshToken == null) {
            logger.info("No authentication tokens found, returning 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No authentication token found");
            return;
        }

        try {
            if (accessToken != null && jwtUtil.isTokenValid(accessToken)) {
                logger.info("Validating access token");
                String username = jwtUtil.getClaimsFromToken(accessToken).getSubject();
                logger.info("Access token valid for user: {}", username);
                User user = userService.getUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication set for user: {}", username);
            }
            else if(refreshToken != null && jwtUtil.isTokenValid(refreshToken)) {
                logger.info("Validating refresh token");
                String username = jwtUtil.getClaimsFromToken(refreshToken).getSubject();
                logger.info("Refresh token valid for user: {}", username);
                User user = userService.getUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication set for user: {}", username);

                Map<String, Object> claims = Map.of("username", username);
                String newAccessToken = jwtUtil.generateAccessToken(claims, username);
                Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
                newAccessTokenCookie.setPath("/");
                newAccessTokenCookie.setMaxAge(60 * 60);
                newAccessTokenCookie.setHttpOnly(true);
                newAccessTokenCookie.setSecure(true);
                response.addCookie(newAccessTokenCookie);
                logger.info("New access token generated and set in cookie");
            }
            else {
                logger.info("Both tokens are invalid or expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh token is invalid or expired");
                return;
            }
            logger.info("Proceeding with authenticated request");
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed: " + e.getMessage());
            return;
        }
    }
}
