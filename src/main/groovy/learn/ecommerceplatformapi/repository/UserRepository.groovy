package learn.ecommerceplatformapi.repository

import learn.ecommerceplatformapi.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username)
    Boolean existsByUsername(String username)
    Boolean existsByEmail(String email)
}