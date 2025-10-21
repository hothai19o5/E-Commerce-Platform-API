package learn.ecommerceplatformapi.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ReviewRequest {
    @NotBlank
    @Min(1)
    @Max(5)
    Integer rating
    String comment
}
