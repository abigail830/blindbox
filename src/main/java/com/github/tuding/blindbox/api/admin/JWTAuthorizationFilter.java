package com.github.tuding.blindbox.api.admin;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    Jwt jwt;

    final List<String> excludedPaths = Arrays.asList("css", "index", "login",
            "/swagger-ui.html", "/swagger-resources", "/webjars", "/v2/api-docs",
            "/wx", "/callback", "/actuator", "/dashboard");

    public boolean isExcludedPath(String path) {
        for (String excludedPath : excludedPaths) {
            if (path.contains(excludedPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie[] cookies = httpServletRequest.getCookies();
        log.debug("{}", httpServletRequest.getServletPath());

        if (!isExcludedPath(httpServletRequest.getServletPath())) {
            try {
                for (Cookie cookie : cookies) {
                    if ("adminToken".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        DecodedJWT verify = jwt.verifyWxToken(token);
                        log.info("Logged in with user: {}", verify.getClaim("user").asString());
                        filterChain.doFilter(httpServletRequest, httpServletResponse);
                        return;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.info("redirect to login page");
                httpServletResponse.sendRedirect("/index");
                return;
            }
            log.info("redirect to login page");
            httpServletResponse.sendRedirect("/index");
            return;
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
