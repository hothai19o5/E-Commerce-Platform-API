package learn.ecommerceplatformapi.controller

import jakarta.validation.Valid
import learn.ecommerceplatformapi.dto.request.OrderRequest
import learn.ecommerceplatformapi.dto.response.OrderDetailResponse
import learn.ecommerceplatformapi.dto.response.OrderResponse
import learn.ecommerceplatformapi.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController {

    @Autowired private OrderService orderService

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity checkoutOrder(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponse result = orderService.checkout(user, request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity getUserOrders(@AuthenticationPrincipal UserDetails user) {
        def result = orderService.getOrders(user)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity getOrderDetails(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long orderId
    ) {
        OrderDetailResponse result = orderService.getOrderDetail(user, orderId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity cancelOrder(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long orderId
    ) {
        def result = orderService.cancelOrder(user, orderId)
        return ResponseEntity.ok(result)
    }
}
