// Filter kiểm tra JWT trong mỗi request
package learn.ecommerceplatformapi.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import learn.ecommerceplatformapi.security.sevices.UserDetailsServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter)

    @Autowired
    JwtUtils jwtUtils

    @Autowired
    UserDetailsServiceImpl userDetailsService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request)
            if (jwt && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt)
                def userDetails = userDetailsService.loadUserByUsername(username)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request))
                SecurityContextHolder.getContext().setAuthentication(authentication)
            }
        } catch (Exception e) {
            logger.debug("JWT filter error: {}", e.message)
        }

        filterChain.doFilter(request, response)
    }

    private static String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization")
        if (headerAuth && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7)
        }
        return null
    }
}
