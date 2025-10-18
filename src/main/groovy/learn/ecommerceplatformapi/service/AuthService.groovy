// Logic xử lý đăng ký, đăng nhập
package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.dto.request.LoginRequest
import learn.ecommerceplatformapi.dto.request.RegisterRequest
import learn.ecommerceplatformapi.dto.request.TokenRefreshRequest
import learn.ecommerceplatformapi.dto.response.JwtResponse
import learn.ecommerceplatformapi.dto.response.MessageResponse
import learn.ecommerceplatformapi.dto.response.TokenRefreshResponse
import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.entity.Role
import learn.ecommerceplatformapi.entity.User
import learn.ecommerceplatformapi.exeception.TokenRefreshException
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

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    @Autowired
    PasswordEncoder encoder

    @Autowired
    JwtUtils jwtUtils

    @Autowired
    RefreshTokenService refreshTokenService

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
    MessageResponse registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return new MessageResponse("Error: Username is already taken!")
        }
        if (userRepository.existsByEmail(signUpRequest.email)) {
            return new MessageResponse("Error: Email is already in use!")
        }

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
                    .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."))
            roles.add(userRole)
        } else {
            strRoles.each { String role ->
                switch (role?.toLowerCase()) {
                    case 'admin':
                        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found.")))
                        break
                    case 'mod':
                    case 'moderator':
                        roles.add(roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role MODERATOR is not found.")))
                        break
                    default:
                        roles.add(roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found.")))
                }
            }
        }

        user.roles = roles
        userRepository.save(user)

        return new MessageResponse("User registered successfully!")
    }

    TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestToken = request.refreshToken
        def refreshToken = refreshTokenService.findByToken(requestToken)
                .map { refreshTokenService.verifyExpiration(it) }
                .orElseThrow( new TokenRefreshException(requestToken, "Refresh toekn not found"))

        String newAccessToken = jwtUtils.generateTokenFromUsername(refreshToken.user.username)
        def rotated = refreshTokenService.rotate(refreshToken)

        return new TokenRefreshResponse(newAccessToken, rotated.token)
    }

    @Transactional
    MessageResponse logoutUser(String requestToken) {
        refreshTokenService.revokeByToken(requestToken)
        return new MessageResponse("Log out successful!")
    }
}
