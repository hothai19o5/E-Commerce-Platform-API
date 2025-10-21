package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.entity.User
import learn.ecommerceplatformapi.repository.RoleRepository
import learn.ecommerceplatformapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AdminUserService {

    @Autowired private UserRepository userRepository
    @Autowired private RoleRepository roleRepository

    def getAllUsers(Integer page, Integer limit, String search) {
        if (page < 1) page = 1
        if (limit < 1) limit = 10

        def pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())

        def usersPage = search ?
                userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable)
                : userRepository.findAll(pageable)

        return [
                content: usersPage.content.collect { user ->
                    [
                            id       : user.id,
                            username : user.username,
                            email    : user.email,
                            roles    : user.roles*.name,
                            createdAt: user.createdAt
                    ]
                },
                totalPage: usersPage.totalPages,
                totalElement: usersPage.totalElements
        ]
    }

    @Transactional
    def deleteUser(Long userId) {
        def user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: ${userId}"))
        userRepository.delete(user)
    }

    @Transactional
    def updateUserRole(Long userId, String roleName) {
        def user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: ${userId}"))

        def roleEnum
        try {
            roleEnum = ERole.valueOf("ROLE_${roleName.toUpperCase()}")
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role. Must be CUSTOMER or ADMIN")
        }

        def role = roleRepository.findByName(roleEnum)
                .orElseThrow { new IllegalArgumentException("Role not found: ${roleEnum}") }

        user.roles.clear()
        user.roles.add(role)
        userRepository.save(user)
    }
}
