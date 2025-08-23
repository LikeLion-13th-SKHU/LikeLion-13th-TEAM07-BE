package com.example.ie_um.global.annotation.resolver;

import com.example.ie_um.global.annotation.AuthenticatedId;
import com.example.ie_um.global.annotation.exception.UnauthorizedException;
import com.example.ie_um.global.jwt.JwtProvider;
import com.example.ie_um.member.domain.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticatedIdResolver implements HandlerMethodArgumentResolver {
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("Authorization 헤더가 없거나 형식이 잘못되었습니다.");
        }
        String token = authHeader.substring(BEARER_PREFIX.length());

        Long userId = jwtProvider.extractUserIdFromToken(token);

        if (userId == null) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다: 사용자 ID 정보를 찾을 수 없습니다.");
        }
        return userId;
    }
}
