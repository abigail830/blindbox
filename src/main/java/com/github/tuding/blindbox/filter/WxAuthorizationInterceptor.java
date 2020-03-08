package com.github.tuding.blindbox.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.security.JWTTokenHandler;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class WxAuthorizationInterceptor implements HandlerInterceptor {

    final List<String> excludedPaths = Arrays.asList(
            "/swagger-ui.html", "/swagger-resources", "/webjars", "/v2/api-docs");
    @Autowired
    JWTTokenHandler jwtTokenHandler;

    public boolean isExcludedPath(String path) {
        for (String excludedPath : excludedPaths) {
            if (path.contains(excludedPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(IgnoreWxVerifyToken.class) || isExcludedPath(request.getServletPath())) {
            return true;
        } else {
            String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
            if (token == null) {
                return verificationFail(response, "No Token, please login first.");
            } else {
                try {
                    jwtTokenHandler.verifyWxToken(token);
                } catch (TokenExpiredException tokenExpiredException) {
                    return verificationFail(response, "Token expired");
                } catch (JWTVerificationException exception) {
                    return verificationFail(response, "Invalid signature/claims");
                }
                return true;
            }
        }
    }

    private boolean verificationFail(HttpServletResponse response, String reason) throws IOException {
        log.error(reason);
        response.getWriter().write(JsonUtil.toJson(reason));
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
