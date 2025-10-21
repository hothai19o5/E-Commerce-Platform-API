package learn.ecommerceplatformapi.controller

import learn.ecommerceplatformapi.entity.Cart
import learn.ecommerceplatformapi.service.CartItemService
import learn.ecommerceplatformapi.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cart")
class CartController {

    @Autowired private CartService cartService
    @Autowired private CartItemService cartItemService

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity getCart(@AuthenticationPrincipal UserDetails user) {
        Cart result = cartService.getCartByUser(user)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/cart/items")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity addItemToCart(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody Map<String, Object> request
    ) {
        Cart result = cartItemService.addItemToCart(user, request)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/cart/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity updateCartItem(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody Map<String, Object> request,
            @PathVariable Long cartItemId
    ) {
        Cart result = cartItemService.updateCartItem(user, request, cartItemId)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity deleteCartItem(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long cartItemId
    ) {
        Cart cart = cartService.deleteCartItem(user, cartItemId)
        Map<String, Object> result = ["message": "Cart item deleted successfully", "cart": cart]
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/cart")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity clearCart(@AuthenticationPrincipal UserDetails user) {
        Cart result = cartService.clearCart(user)
        return ResponseEntity.ok(result)
    }

}
