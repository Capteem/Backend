package com.plog.demo.config.interceptor;

import com.plog.demo.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
@Slf4j
public class LogInInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("[LogInInterceptor] 인터셉터 실행");
        String token = jwtTokenProvider.resolveToken(request);

        if(isNotValidatedToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }

    private boolean isNotValidatedToken(String token) {
        return token == null || !jwtTokenProvider.validationToken(token) || !jwtTokenProvider.isAccessToken(token);
    }
}
