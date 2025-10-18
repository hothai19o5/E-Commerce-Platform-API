package learn.ecommerceplatformapi.dto.response

class JwtResponse {
    String token
    String type = "Bearer"
    String refreshToken

    Long id
    String username
    String email
    List<String> roles

    JwtResponse(String accessToken, String refreshToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken
        this.refreshToken = refreshToken
        this.id = id
        this.username = username
        this.email = email
        this.roles = roles
    }
}
