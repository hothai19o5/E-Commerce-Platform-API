package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.entity.Cart
import learn.ecommerceplatformapi.repository.CartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class CartService {
    @Autowired CartRepository cartRepository

    Cart getCartByUser(UserDetails user) {
        if (user == null || user.username == null)
            throw new IllegalArgumentException("Invalid user details")

        Cart result = cartRepository.findByUserUsername(user.username)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"))

        return result
    }

    @Transactional
    Cart deleteCartItem(UserDetails user, Long cartItemId) {
        if (user == null || user.username == null)
            throw new IllegalArgumentException("Invalid user details")

        if (cartItemId == null || cartItemId <= 0)
            throw new IllegalArgumentException("Invalid cart item ID")

        Cart cart = cartRepository.findByUserUsername(user.username)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"))

        def itemToRemove = cart.items.find { it.id == cartItemId }
        if (itemToRemove) {
            cart.items.remove(itemToRemove)
            cartRepository.save(cart)
            return cart
        } else {
            throw new NoSuchElementException("Cart item not found with id: $cartItemId")
        }
    }

    @Transactional
    Cart clearCart(UserDetails user) {
        if (user == null || user.username == null)
            throw new IllegalArgumentException("Invalid user details")

        Cart cart = cartRepository.findByUserUsername(user.username)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"))

        cart.items.clear()
        cartRepository.save(cart)
        return cart
    }
}
