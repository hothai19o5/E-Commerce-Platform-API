package learn.ecommerceplatformapi.dto.response

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ReviewResponse {
    Long reviewId
    Long userId
    String username
    Integer rating
    String comment
    String createdAt
}
