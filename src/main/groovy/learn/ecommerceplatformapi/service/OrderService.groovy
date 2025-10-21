package learn.ecommerceplatformapi.service

import com.stripe.exception.StripeException
import com.stripe.model.Refund
import com.stripe.param.RefundCreateParams
import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.dto.AddressDTO
import learn.ecommerceplatformapi.dto.OrderItemDTO
import learn.ecommerceplatformapi.dto.request.OrderRequest
import learn.ecommerceplatformapi.dto.response.OrderDetailResponse
import learn.ecommerceplatformapi.dto.response.OrderResponse
import learn.ecommerceplatformapi.entity.EShippingMethod
import learn.ecommerceplatformapi.entity.EStatusOrder
import learn.ecommerceplatformapi.entity.EStatusPayment
import learn.ecommerceplatformapi.entity.Order
import learn.ecommerceplatformapi.repository.CartRepository
import learn.ecommerceplatformapi.repository.OrderRepository
import learn.ecommerceplatformapi.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class OrderService {

    @Autowired OrderRepository orderRepository
    @Autowired CartRepository cartRepository
    @Autowired ProductRepository productRepository

    @Transactional
    OrderResponse checkout(UserDetails user, OrderRequest request) {
        def cart = cartRepository.findByUserUsername(user.username)
                .orElseThrow { new IllegalArgumentException("Cart not found") }

        if (cart.items.isEmpty())
            throw new IllegalArgumentException("Cart is empty")

        def subtotal = 0d
        cart.items.each { item ->
            def product = productRepository.findById(item.product.id)
                    .orElseThrow { new IllegalArgumentException("Product not found: ${item.product.id}") }

            if (product.stockQuantity < item.quantity)
                throw new IllegalArgumentException("Insufficient stock for product: ${product.name}")

            subtotal += product.price * item.quantity
            product.stockQuantity -= item.quantity
            productRepository.save(product)
        }
        // Can get shipping cost from api of shipping provider
        def shippingCost = request.shippingMethod == EShippingMethod.EXPRESS ? 20.0 : 10.0
        def tax = subtotal * 0.1
        def total = subtotal + shippingCost + tax

        Order order = Order.builder()
                .user(cart.user)
                .items(new ArrayList<>(cart.items))
                .shippingAddress(request.shippingAddress)
                .billingAddress(request.billingAddress)
                .shippingMethod(request.shippingMethod)
                .subtotal(subtotal)
                .shippingCost(shippingCost)
                .tax(tax)
                .total(total)
                .status(EStatusOrder.PENDING_PAYMENT)
                .build()

        def savedOrder = orderRepository.save(order)

        cart.items.clear()
        cartRepository.save(cart)

        return OrderResponse.builder()
                .orderId(savedOrder.id)
                .userId(savedOrder.user.id)
                .items(savedOrder.items.collect { item ->
                    new OrderItemDTO(
                            productId: item.product.id,
                            productName: item.product.name,
                            quantity: item.quantity,
                            price: item.product.price
                    )
                })
                .subtotal(savedOrder.subtotal)
                .shippingCost(savedOrder.shippingCost)
                .tax(savedOrder.tax)
                .total(savedOrder.total)
                .status(savedOrder.status)
                .createdAt(savedOrder.createdAt)
                .build()
    }

    def getOrders(UserDetails user) {
        def orders = orderRepository.findByUserUsername(user.username)
                .orElseThrow { new IllegalArgumentException("No orders found for user: ${user.username}") }
        Map<String, Object> result = [:]
        result.orders = orders.collect { order ->
            [
                    orderId  : order.id,
                    total    : order.total,
                    status   : order.status,
                    createdAt: order.createdAt
            ]
        }
        return result
    }

    def getOrderDetail(UserDetails user, Long orderId) {
        def order = orderRepository.findById(orderId)
                .orElseThrow { new IllegalArgumentException("Order not found: $orderId") }

        if (order.user.username != user.username)
            throw new IllegalAccessException("You are not authorized to view this order")

        OrderDetailResponse result = [:]
        result.orderId = order.id
        result.items = order.items.collect { item ->
            new OrderItemDTO(
                    productId: item.product.id,
                    productName: item.product.name,
                    quantity: item.quantity,
                    price: item.product.price
            )
        }
        result.subtotal = order.subtotal
        result.shippingCost = order.shippingCost
        result.tax = order.tax
        result.total = order.total
        result.status = order.status
        result.shippingAddress = new AddressDTO(
                fullName: order.shippingAddress.fullName,
                city: order.shippingAddress.city,
                state: order.shippingAddress.state,
                zipCode: order.shippingAddress.zipCode,
                country: order.shippingAddress.country
        )
        result.paymentId = order.payment.id
        result.paidAt = order.paidAt
        result.shippedAt = order.shippedAt
        result.deliveredAt = order.deliveredAt
        result.trackingNumber = order.trackingNumber
        return result
    }

    @Transactional
    def cancelOrder(UserDetails user, Long orderId) {
        def order = orderRepository.findById(orderId)
                .orElseThrow { new IllegalArgumentException("Order not found: $orderId") }

        if (order.user.username != user.username)
            throw new IllegalAccessException("You are not authorized to cancel this order")

        if (order.status in [EStatusOrder.SHIPPED, EStatusOrder.DELIVERED, EStatusOrder.CANCELED])
            throw new IllegalStateException("Order cannot be canceled")

        // Refund logic can be added here if payment was completed
        def refundId = null
        try {
            if (order.payment != null && order.payment.status == EStatusPayment.COMPLETED) {
                RefundCreateParams params = RefundCreateParams.builder()
                        .setPaymentIntent(order.payment.stripePaymentId)
                        .build()
                Refund refund = Refund.create(params)
                refundId = refund.id
            }
            restoreInventory(order)
        } catch (StripeException ex) {
            throw new RuntimeException("Refund failed: ${ex.message}")
        }

        order.status = EStatusOrder.CANCELED
        order.canceledAt = LocalDateTime.now()
        orderRepository.save(order)

        return ["message": "Order canceled", "refundId": refundId]
    }

    @Transactional
    def restoreInventory(Order order) {
        order.items.each { item ->
            def product = productRepository.findById(item.product.id)
                    .orElseThrow { new IllegalArgumentException("Product not found: ${item.product.id}") }
            product.stockQuantity += item.quantity
            productRepository.save(product)
        }
    }
}
