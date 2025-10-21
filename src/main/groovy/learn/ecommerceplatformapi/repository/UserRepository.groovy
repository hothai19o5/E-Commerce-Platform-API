package learn.ecommerceplatformapi.repository

import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username)
    Boolean existsByUsername(String username)
    Boolean existsByEmail(String email)
    @Query("""
        SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :role
    """)
    Long countByRole(ERole role)

    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email, Pageable pageable)
}