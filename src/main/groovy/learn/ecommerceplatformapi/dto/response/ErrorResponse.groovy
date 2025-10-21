package learn.ecommerceplatformapi.dto.response

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ErrorResponse {
    String message

    ErrorResponse(String message) {
        this.message = message
    }
}
