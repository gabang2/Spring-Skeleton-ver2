package com.project.global.config.jwt;

import com.project.global.config.redis.RedisService;
import com.project.global.error.exception.BusinessException;
import com.project.global.error.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.auth.Subject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RedisService redisService;

    // 실제 JWT 검증을 실행하는 Provider
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 인증에서 제외할 url
    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/static/**",
                            "/favicon.ico",
                            "/api/members/register",
                            "/api/members/login"
                    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        String memberId = null;
        String jwtToken = null;
        String typ = null;

        // Bearer token인 경우 JWT 토큰 유효성 검사 진행
        if (token != null && token.startsWith("Bearer ")) {
            jwtToken = token.substring(7);
            try {
                memberId = jwtTokenProvider.getSubFromToken(jwtToken);
                String subject = jwtTokenProvider.getSubjectFromToken(jwtToken);
                Boolean refreshPathNotValid = subject.equals("refresh-token") && !request.getRequestURI().equals("/api/members/reissue");
                Boolean accessPathNotValid = subject.equals("access-token") && request.getRequestURI().equals("/api/members/reissue");

                if (refreshPathNotValid){
                    log.error("요청 경로가 잘못되었습니다.");
                    throw new BusinessException(ErrorCode.CANT_REISSUE);
                } if (accessPathNotValid){
                    log.error("요청 경로가 잘못되었습니다.");
                    throw new BusinessException(ErrorCode.CANT_REISSUE);
                } else if (subject.equals("refresh-token")) {
                    jwtTokenProvider.verifiedRefreshToken(token);
                }
            } catch (SignatureException e) {
                log.error("Invalid JWT signature: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                log.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.error("JWT claims string is empty: {}", e.getMessage());
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // token 검증이 되고 인증 정보가 존재하지 않는 경우 spring security 인증 정보 저장
        if(memberId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if(jwtTokenProvider.validateToken(jwtToken)) {
                
                UsernamePasswordAuthenticationToken authenticationToken =
                        // Principal, credentials, authorities
                        new UsernamePasswordAuthenticationToken(memberId, null , null);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }

    // Filter에서 제외할 URL 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
