package learn.ecommerceplatformapi.exeception

class UsernameOrEmailAlreadyExistsException extends RuntimeException {
    UsernameOrEmailAlreadyExistsException(String usernameOrEmail) {
        super("Username or Email '" + usernameOrEmail + "' is already taken");
    }
}
