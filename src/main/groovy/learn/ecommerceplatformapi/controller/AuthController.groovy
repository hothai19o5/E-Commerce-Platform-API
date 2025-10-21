package learn.ecommerceplatformapi.controller

import jakarta.validation.Valid
import learn.ecommerceplatformapi.dto.request.LoginRequest
import learn.ecommerceplatformapi.dto.request.RegisterRequest
import learn.ecommerceplatformapi.dto.request.TokenRefreshRequest
import learn.ecommerceplatformapi.dto.response.JwtResponse
import learn.ecommerceplatformapi.dto.response.ErrorResponse
import learn.ecommerceplatformapi.dto.response.TokenRefreshResponse
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

    @Autowired private AuthService authService

    @PostMapping("/signin")
    ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest))
    }

    @PostMapping("/signup")
    ResponseEntity registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        return ResponseEntity.ok(authService.registerUser(signUpRequest))
    }

    @PostMapping("/logout")
    ResponseEntity logout(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.logoutUser(request.refreshToken))
    }

    @PostMapping("/refreshtoken")
    ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request))
    }
}
