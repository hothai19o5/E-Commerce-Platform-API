package learn.ecommerceplatformapi.dto.request

import jakarta.validation.constraints.NotBlank

class TokenRefreshRequest {
    @NotBlank
    String refreshToken
}
