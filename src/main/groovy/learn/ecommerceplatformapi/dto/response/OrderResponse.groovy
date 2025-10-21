package learn.ecommerceplatformapi.dto.response

import learn.ecommerceplatformapi.dto.OrderItemDTO
import learn.ecommerceplatformapi.entity.EStatusOrder
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

import java.time.LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class OrderResponse {
    Long orderId
    Long userId
    List<OrderItemDTO> items
    Double subtotal
    Double tax
    Double total
    EStatusOrder status
    LocalDateTime createdAt
}
