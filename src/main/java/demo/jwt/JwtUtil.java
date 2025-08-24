package demo.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Component
@Slf4j
public class JwtUtil {
    private static final String SECRET_STRING = "yFlZ-VS_lgmx304a1JV1gJbn9pQa3SjNmTJdryQ9tOc";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60L; // 5 hours


    public String createToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000L))
                .signWith(SECRET_KEY)
                .compact();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        if (claims == null) {
            log.warn("Failed to extract claims from token");
            return null;
        }

        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);

        return expirationDate.before(new Date());
    }


    public boolean validateToken(String token) {
        if (!isTokenSignatureValid(token)) {
            log.warn("Token validation failed: token signature is invalid");
            return false;
        }

        String tokenUsername = extractUsername(token);

        if (StringUtils.isBlank(tokenUsername)) {
            log.warn("Token validation failed: tokenUsername in the token is blank");
            return false;
        }

        if (isTokenExpired(token)) {
            log.warn("Token validation failed: token is expired");
            return false;
        }

        return true;
    }


    public boolean isTokenSignatureValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // token过期（但签名有效）
            log.debug("Token expired but signature is valid", e);
            return false;
        } catch (UnsupportedJwtException e) {
            // 不支持的JWT格式
            log.warn("Unsupported JWT token", e);
            return false;
        } catch (MalformedJwtException e) {
            // 格式错误的JWT
            log.warn("Malformed JWT token", e);
            return false;
        } catch (JwtException  e) {
            // 签名验证失败
            log.warn("JWT validation failed", e);
            return false;
        } catch (IllegalArgumentException e) {
            // 空或null token
            log.warn("JWT token is null or empty", e);
            return false;
        } catch (SecurityException e) {
            // 新API：SecurityException 替代了 SignatureException
            log.warn("Token signature validation failed", e);
            return false;
        }
    }

}
