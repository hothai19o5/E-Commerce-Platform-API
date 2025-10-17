// Xử lý lỗi xác thực
package learn.ecommerceplatformapi.security.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt)

    @Override
    void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        logger.error("Unauthorized error: {}", authException.message)
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.print('{"message":"Error: Unauthorized"}')
    }
}
