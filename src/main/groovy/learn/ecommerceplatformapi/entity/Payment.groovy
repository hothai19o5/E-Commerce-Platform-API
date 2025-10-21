package learn.ecommerceplatformapi.entity

import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "payments")
class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    Order order

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user

    @Column(name = "stripe_payment_id")
    String stripePaymentId

    Double amount

    @Enumerated(EnumType.STRING)
    EStatusPayment status

    @Column(name = "payment_method")
    String paymentMethod

    LocalDateTime createdAt = LocalDateTime.now()

    LocalDateTime completedAt = null
}
