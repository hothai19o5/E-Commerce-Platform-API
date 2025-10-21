package learn.ecommerceplatformapi.dto.response

import learn.ecommerceplatformapi.dto.AddressDTO
import learn.ecommerceplatformapi.dto.OrderItemDTO
import learn.ecommerceplatformapi.entity.EStatusOrder
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

import java.time.LocalDateTime

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class OrderDetailResponse {
    Long orderId
    Long userId
    List<OrderItemDTO> items
    Double subtotal
    Double tax
    Double total
    EStatusOrder status
    Double shippingCost
    AddressDTO shippingAddress
    Long paymentId
    LocalDateTime paidAt
    LocalDateTime shippedAt
    LocalDateTime deliveredAt
    String trackingNumber
}
