package learn.ecommerceplatformapi.configuration

import com.stripe.Stripe
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class StripeConfig {
    @Value('${stripe.secret-key}') String secretKey

    @PostConstruct
    void init() {
        Stripe.apiKey = secretKey
    }
}
