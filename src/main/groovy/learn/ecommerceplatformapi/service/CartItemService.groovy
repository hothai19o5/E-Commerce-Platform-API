package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.entity.Cart
import learn.ecommerceplatformapi.repository.CartItemRepository
import learn.ecommerceplatformapi.repository.CartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class CartItemService {

    @Autowired private CartItemRepository cartItemRepository
    @Autowired private CartRepository cartRepository

    @Transactional
    Cart addItemToCart(UserDetails user, Map<String, Object> request) {
        if (user == null || user.username == null)
            throw new IllegalArgumentException("Invalid user details")

        Long productId = request.get("productId") as Long
        Integer quantity = request.get("quantity") as Integer
        if (productId == null || quantity == null || quantity <= 0)
            throw new IllegalArgumentException("Invalid product ID or quantity")

        cartItemRepository.addItemToCart(user.username, productId, quantity)

        return cartRepository.findByUserUsername(user.username).orElseThrow(() -> new IllegalArgumentException("Cart not found for user"))
    }

    @Transactional
    def updateCartItem(UserDetails user, Map<String, Object> request, Long cartItemId) {
        if (user == null || user.username == null)
            throw new IllegalArgumentException("Invalid user details")

        Integer quantity = request.get("quantity") as Integer
        if (cartItemId == null || quantity == null || quantity <= 0 || cartItemId <= 0)
            throw new IllegalArgumentException("Invalid cart item ID or quantity")

        cartItemRepository.updateCartItem(user.username, cartItemId, quantity)

        return cartRepository.findByUserUsername(user.username).orElseThrow(() -> new IllegalArgumentException("Cart not found for user"))
    }
}
