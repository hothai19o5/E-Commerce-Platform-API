package learn.ecommerceplatformapi.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

import java.time.LocalDateTime

@Entity
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    User user

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CartItem> items = new HashSet<>()

    LocalDateTime createdAt = LocalDateTime.now()

    LocalDateTime updatedAt = LocalDateTime.now()
}
