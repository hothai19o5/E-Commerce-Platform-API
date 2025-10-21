package learn.ecommerceplatformapi.configuration

import jakarta.annotation.PostConstruct
import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.entity.EShippingMethod
import learn.ecommerceplatformapi.entity.Role
import learn.ecommerceplatformapi.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DataInitializer {

    @Autowired
    private RoleRepository roleRepository

    @PostConstruct
    void init() {
        // Seed default roles if not present
        [ERole.ROLE_USER, ERole.ROLE_MODERATOR, ERole.ROLE_ADMIN].each { ERole r ->
            roleRepository.findByName(r).orElseGet({ -> roleRepository.save(new Role(r)) })
        }
    }
}

