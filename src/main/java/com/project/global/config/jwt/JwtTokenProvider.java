package com.project.global.config.jwt;

import com.project.domain.member.dto.MemberPatchRequestDto;
import com.project.domain.member.entity.Member;
import com.project.domain.member.service.MemberService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static String secret = "jangdaehyeok";

    // 1시간 단위
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // token으로 subject(access-token, refresh-token 여부) 조회
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // token으로 사용자 id 조회
    public String getSubFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    // token으로 사용자 속성정보 조회
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 모든 token에 대한 사용자 속성정보 조회
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // 토근 만료 여부 체크
	/*
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	*/

    // 토큰 만료일자 조회
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // id를 입력받아 accessToken 생성
    public String generateAccessToken(String id) {
        return generateAccessToken(id, new HashMap<>());
    }

    public String generateAccessToken(Long id) {
        return generateAccessToken(id.toString(), new HashMap<>());
    }

    // id, 속성정보를 이용해 accessToken 생성
    public String generateAccessToken(String id, Map<String, Object> claims) {
        return doGenerateAccessToken(id, claims);
    }

    // JWT accessToken 생성
    private String doGenerateAccessToken(String id, Map<String, Object> claims) {
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1))// 1시간
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject("access-token")
                .compact();

        return "Bearer " + accessToken;
    }

    // id를 입력받아 accessToken 생성
    public String generateRefreshToken(String id) {
        return doGenerateRefreshToken(id);
    }
    public String generateRefreshToken(Long id) {
        return doGenerateRefreshToken(id.toString());
    }

    // JWT refreshToken 생성
    private String doGenerateRefreshToken(String id) {
        String refreshToken = Jwts.builder()
                .setId(id)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 5)) // 5시간
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject("refresh-token")
                .compact();

        return "Bearer " + refreshToken;
    }

    // id를 입력받아 accessToken, refreshToken 생성
    public Map<String, String> generateTokenSet(String id) {
        return generateTokenSet(id, new HashMap<>());
    }

    public Map<String, String> generateTokenSet(Long id) {
        return generateTokenSet(id.toString(), new HashMap<>());
    }

    // id, 속성정보를 이용해 accessToken, refreshToken 생성
    public Map<String, String> generateTokenSet(String id, Map<String, Object> claims) {
        return doGenerateTokenSet(id, claims);
    }

    // JWT accessToken, refreshToken 생성
    private Map<String, String> doGenerateTokenSet(String id, Map<String, Object> claims) {
        Map<String, String> tokens = new HashMap<String, String>();

        String accessToken = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1))// 1시간
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject("access-token")
                .compact();

        String refreshToken = "Bearer " + Jwts.builder()
                .setId(id)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 5)) // 5시간
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject("refresh-token")
                .compact();

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    // redis로 구현하기
    // JWT refreshToken 만료체크 후 재발급
//    public Boolean reGenerateRefreshToken(String memberId) throws Exception {
//        log.info("[reGenerateRefreshToken] refreshToken 재발급 요청");
//        // 관리자 정보 조회
//        Member member = memberService.verifiedMember(Long.parseLong(memberId));
//        String memberRefreshToken = member.getRefreshToken();
//
//        // refreshToken 정보가 존재하지 않는 경우
//        if(memberRefreshToken == null) {
//            log.info("[reGenerateRefreshToken] refreshToken 정보가 존재하지 않습니다.");
//            return false;
//        }
//
//        // refreshToken 만료 여부 체크
//        try {
//            String refreshToken = memberRefreshToken.substring(7);
//            Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken);
//            log.info("[reGenerateRefreshToken] refreshToken이 만료되지 않았습니다.");
//            return true;
//        }
//        // refreshToken이 만료된 경우 재발급
//        catch(ExpiredJwtException e) {
//            String refreshToken = "Bearer " + generateRefreshToken(memberId);
//            memberService.patchMember(Long.parseLong(memberId), MemberPatchRequestDto.builder().refreshToken(refreshToken).build());
//            log.info("[reGenerateRefreshToken] refreshToken 재발급 완료 : {}", refreshToken);
//            return true;
//        }
//        // 그 외 예외처리
//        catch(Exception e) {
//            log.error("[reGenerateRefreshToken] refreshToken 재발급 중 문제 발생 : {}", e.getMessage());
//            return false;
//        }
//    }

    // 토근 검증
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
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

        return false;
    }
}
