package learn.ecommerceplatformapi.exeception

class TokenRefreshException extends RuntimeException {
    final String token

    TokenRefreshException(String token, String message) {
        super(message)
        this.token = token
    }
}
