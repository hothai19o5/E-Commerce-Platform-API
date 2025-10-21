package learn.ecommerceplatformapi.dto.response

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class TopProduct {
    Long productId
    String productName
    Long totalSold
}
