package kisung.template.board.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kisung.template.board.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
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
   * @param userBasicInfo
   * @return Access Token String
   */
  public String createAccessToken(UserDto.UserBasicInfo userBasicInfo) {
    return createToken(userBasicInfo, accessTokenExpTime);
  }


  /**
   * JWT 생성
   * @param userBasicInfo
   * @param expireTime
   * @return JWT String
   */
  private String createToken(UserDto.UserBasicInfo userBasicInfo, long expireTime) {
    Claims claims = Jwts.claims();
    claims.put(TOKEN_USER_ID, userBasicInfo.getUserId());
    claims.put(TOKEN_EMAIL, userBasicInfo.getEmail());
    claims.put(TOKEN_NICKNAME, userBasicInfo.getNickname());

    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime tokenValidity = now.plusSeconds(expireTime);


    return Jwts.builder()
      .setClaims(claims)
      .setIssuedAt(Date.from(now.toInstant()))
      .setExpiration(Date.from(tokenValidity.toInstant()))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }


  /**
   * Token에서 User ID 추출
   * @param token
   * @return User ID
   */
  public Long getUserId(String token) {
    return parseClaims(token).get("userId", Long.class);
  }

  /**
   * JWT 검증
   * @param token
   * @return IsValidate
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }


  /**
   * JWT Claims 추출
   * @param accessToken
   * @return JWT Claims
   */
  public Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}