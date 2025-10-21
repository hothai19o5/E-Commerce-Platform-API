package learn.ecommerceplatformapi.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    ERole name

    @ManyToMany(mappedBy = "roles")
    Set<User> users = new HashSet<>()
}
