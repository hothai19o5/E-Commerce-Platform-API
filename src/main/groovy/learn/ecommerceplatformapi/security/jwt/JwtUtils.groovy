// Tạo và xác thực JWT
package learn.ecommerceplatformapi.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

import javax.crypto.SecretKey

@Service
class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils)

    @Value('${application.security.jwt.secret-key}')
    private String jwtSecret

    @Value('${application.security.jwt.expiration}')
    private long jwtExpirationMs

    private SecretKey getSigningKey() {
        // Secret is configured as hex string in application.yml
        byte[] keyBytes = Decoders.HEX.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    String generateTokenFromUsername(String username) {
        Date now = new Date()
        Date expiryDate = new Date(now.time + jwtExpirationMs)
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact()
    }

    String generateToken(UserDetails userDetails) {
        return generateTokenFromUsername(userDetails.username)
    }

    String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
    }

    boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken)
            return true
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.message)
        }
        return false
    }
}
