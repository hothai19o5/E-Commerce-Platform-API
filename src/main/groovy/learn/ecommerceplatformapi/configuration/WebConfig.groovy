// Cấu hình khác (ví dụ: CORS)
package learn.ecommerceplatformapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class WebConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration()
        configuration.setAllowCredentials(false)
        configuration.setAllowedOriginPatterns(["*"]) // chỉnh theo domain FE khi cần
        configuration.setAllowedMethods(["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
        configuration.setAllowedHeaders(["Authorization", "Cache-Control", "Content-Type", "X-Requested-With"])
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
