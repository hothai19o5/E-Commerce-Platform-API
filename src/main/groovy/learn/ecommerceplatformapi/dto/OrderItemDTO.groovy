package learn.ecommerceplatformapi.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class OrderItemDTO {
    Long productId
    String productName
    Integer quantity
    Double price
}
