package learn.ecommerceplatformapi.repository

import io.lettuce.core.dynamic.annotation.Param
import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.entity.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE CartItem ci
        SET ci.quantity = ci.quantity + :quantity
        WHERE ci.cart.user.username = :username AND ci.product.id = :productId
    """)
    int addItemToCart(@Param("username") String username, @Param("productId") Long productId, @Param("quantity") Integer quantity)

    @Modifying
    @Transactional
    @Query("""
        UPDATE CartItem ci
        SET ci.quantity = :quantity
        WHERE ci.cart.user.username = :username AND ci.id = :cartItemId
    """)
    int updateCartItem(@Param("username") String username, @Param("cartItemId") Long cartItemId, @Param("quantity") Integer quantity)
}