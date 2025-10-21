package learn.ecommerceplatformapi.entity

import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "cart_items")
class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    Cart cart

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product

    Long quantity

    Double price

    LocalDateTime createdAt = LocalDateTime.now()

    LocalDateTime updatedAt = LocalDateTime.now()
}
