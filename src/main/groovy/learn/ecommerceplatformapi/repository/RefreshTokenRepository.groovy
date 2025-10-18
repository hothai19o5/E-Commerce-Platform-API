package learn.ecommerceplatformapi.repository

import learn.ecommerceplatformapi.entity.RefreshToken
import learn.ecommerceplatformapi.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token)
    long deleteByToken(String token)
    long deleteByUser(User user)
}
