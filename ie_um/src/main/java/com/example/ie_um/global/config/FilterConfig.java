package com.example.ie_um.global.config;

import com.example.ie_um.global.filter.LogFilter;
import com.example.ie_um.global.filter.LoginCheckFilter;
import com.example.ie_um.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final JwtProvider jwtProvider;

    @Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogFilter());
        registrationBean.setOrder(1); // 필터 실행 순서(우선순위) 지정 - 낮을 수록 먼저 실행
        registrationBean.addUrlPatterns("/*"); // 모든 요청에 적용
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoginCheckFilter> loginCheckFilter() {
        FilterRegistrationBean<LoginCheckFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginCheckFilter(jwtProvider));
        registrationBean.addUrlPatterns("/api/**"); // 필터를 적용할 URL 패턴
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
