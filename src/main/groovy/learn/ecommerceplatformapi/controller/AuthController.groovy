package learn.ecommerceplatformapi.controller

import jakarta.validation.Valid
import learn.ecommerceplatformapi.dto.request.LoginRequest
import learn.ecommerceplatformapi.dto.request.RegisterRequest
import learn.ecommerceplatformapi.dto.response.JwtResponse
import learn.ecommerceplatformapi.dto.response.MessageResponse
import learn.ecommerceplatformapi.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    AuthService authService

    @PostMapping("/signin")
    ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest))
    }

    @PostMapping("/signup")
    ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        return ResponseEntity.ok(authService.registerUser(signUpRequest))
    }
}
