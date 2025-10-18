package learn.ecommerceplatformapi.security.sevices

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.entity.RefreshToken
import learn.ecommerceplatformapi.entity.User
import learn.ecommerceplatformapi.exeception.TokenRefreshException
import learn.ecommerceplatformapi.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.time.Instant

@Service
class RefreshTokenService {
    @Value('${application.security.jwt.refresh-expiration}')
    private long refreshTokenDurationMs

    @Autowired
    RefreshTokenRepository refreshTokenRepository

    RefreshToken createForUser(User user) {
        def token = new RefreshToken(
                user: user,
                expiryDate: Instant.now().plusMillis(refreshTokenDurationMs),
                token: UUID.randomUUID().toString(),
                revoked: false
        )
        return refreshTokenRepository.save(token)
    }

    Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
    }

    RefreshToken verifyExpiration(RefreshToken token) {
        if (token.expiryDate.isBefore(Instant.now()) || token.revoked) {
            refreshTokenRepository.deleteById(token.id)
            throw new TokenRefreshException(token.token, "Refresh token was expired. Please make a new signin request")
        }
        return token
    }

    @Transactional
    void revokeByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent { t ->
            t.revoked = true
            refreshTokenRepository.save(t)
            refreshTokenRepository.deleteById(t.id)
        }
    }

    @Transactional
    void revokeAllForUser(User user) {
        refreshTokenRepository.deleteByUser(user)
    }

    @Transactional
    RefreshToken rotate(RefreshToken currentToken) {
        refreshTokenRepository.deleteById(currentToken.id)
        return createForUser(currentToken.user)
    }
}
