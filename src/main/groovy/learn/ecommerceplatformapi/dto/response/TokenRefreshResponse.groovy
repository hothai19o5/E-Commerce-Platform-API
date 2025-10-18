package learn.ecommerceplatformapi.dto.response

class TokenRefreshResponse {
    String accessToken
    String refreshToken
    String tokenType = "Bearer"

    TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
}
