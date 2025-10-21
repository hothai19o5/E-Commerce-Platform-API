package learn.ecommerceplatformapi.controller

import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.service.AdminUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
class AdminUserController {

    @Autowired private AdminUserService adminUserService

    @GetMapping
    ResponseEntity getAllUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String search
    ) {
        def result = adminUserService.getAllUsers(page, limit, search)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{userId}")
    ResponseEntity deleteUserById(@PathVariable("userId") Long userId) {
        def result = adminUserService.deleteUser(userId)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/{userId}/role")
    ResponseEntity updateUserRole(
            @PathVariable("userId") Long userId,
            Map<String, ERole> request
    ) {
        def result = adminUserService.updateUserRole(userId, request.get("role").toString())
        return ResponseEntity.ok(result)
    }
}
