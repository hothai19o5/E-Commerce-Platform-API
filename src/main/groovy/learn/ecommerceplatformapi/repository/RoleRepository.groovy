package learn.ecommerceplatformapi.repository

import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name)
}