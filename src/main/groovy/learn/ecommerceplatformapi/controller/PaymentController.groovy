package learn.ecommerceplatformapi.controller

import jakarta.validation.Valid
import learn.ecommerceplatformapi.dto.request.PaymentRequest
import learn.ecommerceplatformapi.dto.response.PaymentResponse
import learn.ecommerceplatformapi.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class PaymentController {

    @Autowired private PaymentService paymentService

    @PostMapping("/{orderId}/pay")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity payForOrder(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody PaymentRequest request,
            @PathVariable Long orderId
    ) {
        PaymentResponse result = paymentService.processPayment(user, orderId, request)
        return ResponseEntity.ok(result)
    }

}
