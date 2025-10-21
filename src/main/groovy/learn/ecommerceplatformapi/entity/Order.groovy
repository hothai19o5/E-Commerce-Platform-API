package learn.ecommerceplatformapi.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

import java.time.LocalDateTime

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user

    Double subtotal

    Double shippingCost

    Double tax

    Double total

    @Enumerated(EnumType.STRING)
    EStatusOrder status

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    Address shippingAddress

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id")
    Address billingAddress

    @Enumerated(EnumType.STRING)
    EShippingMethod shippingMethod

    String trackingNumber

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderItem> items = new HashSet<>()

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    Payment payment

    LocalDateTime createdAt = LocalDateTime.now()

    LocalDateTime paidAt = null

    LocalDateTime shippedAt = null

    LocalDateTime deliveredAt = null

    LocalDateTime canceledAt = null
}
