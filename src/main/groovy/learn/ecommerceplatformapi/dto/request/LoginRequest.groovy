package learn.ecommerceplatformapi.dto.request

import jakarta.validation.constraints.NotBlank

class LoginRequest {
    @NotBlank
    String username
    @NotBlank
    String password
}
