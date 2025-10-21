// Logic xử lý đăng ký, đăng nhập
package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.dto.request.LoginRequest
import learn.ecommerceplatformapi.dto.request.RegisterRequest
import learn.ecommerceplatformapi.dto.request.TokenRefreshRequest
import learn.ecommerceplatformapi.dto.response.JwtResponse
import learn.ecommerceplatformapi.dto.response.ErrorResponse
import learn.ecommerceplatformapi.dto.response.TokenRefreshResponse
import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.entity.Role
import learn.ecommerceplatformapi.entity.User
import learn.ecommerceplatformapi.exeception.TokenRefreshException
import learn.ecommerceplatformapi.exeception.UsernameOrEmailAlreadyExistsException
import learn.ecommerceplatformapi.repository.RoleRepository
import learn.ecommerceplatformapi.repository.UserRepository
import learn.ecommerceplatformapi.security.jwt.JwtUtils
import learn.ecommerceplatformapi.security.sevices.RefreshTokenService
import learn.ecommerceplatformapi.security.sevices.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {

    @Autowired private AuthenticationManager authenticationManager
    @Autowired private UserRepository userRepository
    @Autowired private RoleRepository roleRepository
    @Autowired private PasswordEncoder encoder
    @Autowired private JwtUtils jwtUtils
    @Autowired private RefreshTokenService refreshTokenService

    JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().setAuthentication(authentication)

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.principal
        String jwt = jwtUtils.generateToken(userDetails)

        User user = userRepository.findByUsername(userDetails.username).orElseThrow()
        String refreshToken = refreshTokenService.createForUser(user)

        List<String> roles = userDetails.authorities.collect { it.authority }

        return new JwtResponse(jwt, refreshToken, userDetails.id, userDetails.username, userDetails.email, roles)
    }

    @Transactional
    ErrorResponse registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username))
            throw new UsernameOrEmailAlreadyExistsException(signUpRequest.username)
        if (userRepository.existsByEmail(signUpRequest.email))
            throw new UsernameOrEmailAlreadyExistsException(signUpRequest.email)

        // Create new user's account
        User user = new User(
                username: signUpRequest.username,
                email: signUpRequest.email,
                password: encoder.encode(signUpRequest.password)
        )

        Set<String> strRoles = signUpRequest.role
        Set<Role> roles = new HashSet<>()

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role USER is not found."))
            roles.add(userRole)
        } else {
            strRoles.each { String role ->
                switch (role?.toLowerCase()) {
                    case 'admin':
                        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role ADMIN is not found.")))
                        break
                    case 'moderator':
                        roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Role MODERATOR is not found.")))
                        break
                    default:
                        roles.add(roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Role USER is not found.")))
                }
            }
        }

        user.roles = roles
        userRepository.save(user)

        return new ErrorResponse("User registered successfully!")
    }

    TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestToken = request.refreshToken
        def refreshToken = refreshTokenService.findByToken(requestToken)
                .map { refreshTokenService.verifyExpiration(it) }
                .orElseThrow(() -> new TokenRefreshException(requestToken, "Refresh token is not in database!"))

        String newAccessToken = jwtUtils.generateTokenFromUsername(refreshToken.user.username)
        def rotated = refreshTokenService.rotate(refreshToken)

        return new TokenRefreshResponse(newAccessToken, rotated.token)
    }

    @Transactional
    def logoutUser(String requestToken) {
        refreshTokenService.revokeByToken(requestToken)
    }
}
