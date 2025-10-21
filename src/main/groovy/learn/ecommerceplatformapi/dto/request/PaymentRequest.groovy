package learn.ecommerceplatformapi.dto.request

import jakarta.validation.constraints.NotBlank
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class PaymentRequest {
    @NotBlank
    String stripeTokenId
}
