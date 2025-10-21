package learn.ecommerceplatformapi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

import java.time.LocalDateTime

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String name

    @Column(nullable = false)
    String description

    @Column(nullable = false)
    Double price

    @Column(nullable = false)
    String category

    @Column(name = "stock_quantity",nullable = false)
    Long stockQuantity

    String imageUrl

    LocalDateTime createdAt = LocalDateTime.now()

    LocalDateTime updatedAt = LocalDateTime.now()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    User createdBy

    @OneToMany(mappedBy = "product")
    Set<Review> reviews = new HashSet<>()

    @OneToMany(mappedBy = "product")
    Set<CartItem> cartItems = new HashSet<>()

    @OneToMany(mappedBy = "product")
    Set<OrderItem> orderItems = new HashSet<>()
}
