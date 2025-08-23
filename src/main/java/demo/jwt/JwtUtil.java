package demo.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Component
@Slf4j
public class JwtUtil {
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60L; // 5 hours


    public String createToken(Map<String, Object> claims, String subject) {
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
                .decryptWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);

        return expirationDate.before(new Date());
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        if (userDetails == null) {
            log.warn("Token validation failed: userDetails is null");
            return false;
        }

        String tokenUsername = extractUsername(token);

        if (StringUtils.isBlank(tokenUsername)) {
            log.warn("Token validation failed: tokenUsername in the token is blank");
            return false;
        }

        if (!tokenUsername.equals(userDetails.getUsername())) {
            log.warn("Token validation failed: userDetails username '{}' does not match token tokenUsername '{}'", userDetails.getUsername(), tokenUsername);
            return false;
        }

        if (isTokenExpired(token)) {
            log.warn("Token validation failed: token is expired");
            return false;
        }

        return true;
    }

}
