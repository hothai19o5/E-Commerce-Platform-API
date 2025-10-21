package learn.ecommerceplatformapi.dto.response

class JwtResponse {
    String token
    String type = "Bearer"
    String refreshToken

    Long userId
    String username
    String email
    List<String> roles

    JwtResponse(String token, String refreshToken, Long id, String username, String email, List<String> roles) {
        this.token = token
        this.refreshToken = refreshToken
        this.userId = id
        this.username = username
        this.email = email
        this.roles = roles
    }
}
