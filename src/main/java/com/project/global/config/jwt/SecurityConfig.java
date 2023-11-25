package com.project.global.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * [BCryptPasswordEncoder : 비밀번호를 암호화하는 메서드를 가진 클래스]<br>
     * 메서드 1. encode : 패스워드를 암호화해주는 메서드<br>
     * 메서드 2. matchers : 매개변수 1(인코딩 되지 않은 패스워드), 매개변수 2(인코딩된 패스워드) 와의 일치 여부 boolean으로 반환<br>
     * 메서드 3. upgradeEncoding : 인코딩된 암호를 한 번 더 인코딩하는 경우
     */


    // todo : 사용 용도를 모르겠음.
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain config(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

        // Security 인증을 거치지 않는 url 목록
        MvcRequestMatcher[] PERMIT_ALL_WHITE_LIST = {
                mvc.pattern("/api/members/login"),
                mvc.pattern("/api/members/register")
        };

        // rest api는 stateless 하기 때문에, csrf토큰 정보가 필요 없어서 disable 설정
        http.csrf(AbstractHttpConfigurer::disable);

        // h2와 같은 xframe을 보고자 한다면 disable 설정
        http.headers((headers) ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        // http request 인증 설정
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(PERMIT_ALL_WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
        );

        // 인증 실패 시 exception handler 설정 -> 항상 401에러만 던진다는 단점으로, 추후 Exception Filter 추가 필요
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        // Spring Security에서 session을 생성하거나 사용하지 않도록 설정 - ALWAYS, NEVER, IF_REQUIRED, STATELESS
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Token 유효성 검사
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // todo : Exception Filter 추가

        return http.build();
    }

}
