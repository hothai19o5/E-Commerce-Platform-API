package learn.ecommerceplatformapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    learn.ecommerceplatformapi.entity.ERole name

    Role() {}

    Role(learn.ecommerceplatformapi.entity.ERole name) {
        this.name = name
    }
}
