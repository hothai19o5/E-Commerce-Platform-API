package learn.ecommerceplatformapi.service

import com.stripe.exception.StripeException
import com.stripe.model.Charge
import com.stripe.param.ChargeCreateParams
import learn.ecommerceplatformapi.dto.request.PaymentRequest
import learn.ecommerceplatformapi.dto.response.PaymentResponse
import learn.ecommerceplatformapi.entity.EStatusOrder
import learn.ecommerceplatformapi.entity.EStatusPayment
import learn.ecommerceplatformapi.entity.Payment
import learn.ecommerceplatformapi.repository.OrderRepository
import learn.ecommerceplatformapi.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class PaymentService {

    @Autowired private OrderRepository orderRepository
    @Autowired private PaymentRepository paymentRepository

    PaymentResponse processPayment(UserDetails user, Long orderId, PaymentRequest request) {
        def order = orderRepository.findById(orderId)
                .orElseThrow { new IllegalArgumentException("Order not found: $orderId") }

        if (order.user.username != user.username)
            throw new IllegalAccessException("You are not authorized to pay this order")

        if (order.status != EStatusOrder.PENDING_PAYMENT)
            throw new IllegalStateException("Order already paid")

        try {
            def chargeParams = ChargeCreateParams.builder()
                    .setAmount(order.total * 100 as Long) // cents
                    .setCurrency("usd")
                    .setDescription("Payment for order #$orderId")
                    .setSource(request.stripeTokenId)
                    .build()

            def charge = Charge.create(chargeParams)

            // Save Payment
            def payment = Payment.builder()
                    .order(order)
                    .user(order.user)
                    .amount(order.total)
                    .status(EStatusPayment.COMPLETED)
                    .paymentMethod("STRIPE")
                    .stripePaymentId(charge.paymentIntent)
                    .build()

            paymentRepository.save(payment)

            // Update order
            order.status = EStatusOrder.PROCESSING
            order.paidAt = LocalDateTime.now()
            orderRepository.save(order)

            return PaymentResponse.builder()
                    .paymentId(payment.id)
                    .orderId(order.id)
                    .amount(payment.amount)
                    .status(payment.status)
                    .paidAt(order.paidAt)
                    .build()

        } catch (StripeException ex) {
            throw new RuntimeException("Payment failed: ${ex.message}")
        }
    }
}
