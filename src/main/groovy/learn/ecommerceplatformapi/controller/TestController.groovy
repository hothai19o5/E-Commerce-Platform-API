package learn.ecommerceplatformapi.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/all")
    ResponseEntity<String> allAccess() { ResponseEntity.ok("Public Content.") }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    ResponseEntity<String> userAccess() { ResponseEntity.ok("User Content.") }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    ResponseEntity<String> moderatorAccess() { ResponseEntity.ok("Moderator Board.") }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<String> adminAccess() { ResponseEntity.ok("Admin Board.") }
}

