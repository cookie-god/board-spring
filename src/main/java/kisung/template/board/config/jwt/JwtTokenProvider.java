package kisung.template.board.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import kisung.template.board.common.code.ErrorCode;
import kisung.template.board.common.response.ErrorResponse;
import kisung.template.board.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private static final String TOKEN_USER_ID = "userId";
    private static final String TOKEN_EMAIL = "email";
    private static final String TOKEN_NICKNAME = "nickname";
    private static final String TOKEN_ROLE = "role";
    private final Key key;
    private final long accessTokenExpTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     *
     * @param userBasicInfo
     * @return Access Token String
     */
    public String createAccessToken(UserDto.UserBasicInfo userBasicInfo) {
        return createToken(userBasicInfo, accessTokenExpTime);
    }


    /**
     * JWT 생성
     *
     * @param userBasicInfo
     * @param expireTime
     * @return JWT String
     */
    private String createToken(UserDto.UserBasicInfo userBasicInfo, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put(TOKEN_USER_ID, userBasicInfo.getUserId());
        claims.put(TOKEN_EMAIL, userBasicInfo.getEmail());
        claims.put(TOKEN_NICKNAME, userBasicInfo.getNickname());
        claims.put(TOKEN_ROLE, userBasicInfo.getRole());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Token에서 User ID 추출
     *
     * @param token
     * @return User ID
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    /**
     * JWT 검증
     *
     * @param token
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SignatureException e) {
            log.info("Signature error JWT", e);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json"); // Content-Type 설정
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * JWT Claims 추출
     *
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException | SignatureException e) {
            return null;
        }
    }
}