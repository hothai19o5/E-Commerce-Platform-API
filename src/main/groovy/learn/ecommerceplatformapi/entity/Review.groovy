package learn.ecommerceplatformapi.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

import java.time.LocalDateTime

@Entity
@Table(name = "reviews")
class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    Integer rating

    String comment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user

    LocalDateTime createdAt = LocalDateTime.now()

    LocalDateTime updatedAt = LocalDateTime.now()
}
