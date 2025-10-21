package learn.ecommerceplatformapi.entity

import jakarta.persistence.*

import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(unique = true, nullable = false)
    String username

    @Column(unique = true, nullable = false)
    String email

    @Column(nullable = false)
    String password

    @Column(name = "avatar_url")
    String avatarUrl

    LocalDateTime createdAt = LocalDateTime.now()

    LocalDateTime updatedAt = LocalDateTime.now()

    LocalDateTime deletedAt = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>()

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Address> addresses = new HashSet<>()

    @OneToMany(mappedBy = "createdBy")
    Set<Product> productsCreated = new HashSet<>()

    @OneToMany(mappedBy = "user")
    Set<Order> orders = new HashSet<>()

    @OneToMany(mappedBy = "user")
    Set<Review> reviews = new HashSet<>()

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RefreshToken> refreshTokens = new HashSet<>()

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Cart cart

    @OneToMany(mappedBy = "user")
    Set<Payment> payments = new HashSet<>()
}