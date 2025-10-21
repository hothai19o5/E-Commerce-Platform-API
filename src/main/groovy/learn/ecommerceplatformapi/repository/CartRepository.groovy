package learn.ecommerceplatformapi.repository

import learn.ecommerceplatformapi.entity.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserUsername(String username)
}