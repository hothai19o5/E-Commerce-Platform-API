package learn.ecommerceplatformapi.dto.response

import learn.ecommerceplatformapi.entity.EStatusPayment
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

import java.time.LocalDateTime

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class PaymentResponse {
    Long orderId
    Long paymentId
    EStatusPayment status
    Double amount
    LocalDateTime paidAt
}
