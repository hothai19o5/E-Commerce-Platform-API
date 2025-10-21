package learn.ecommerceplatformapi.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class RegisterRequest {
    @NotBlank
    String username
    @NotBlank
    String email
    @NotNull
    Set<String> role
    @NotBlank
    String password
}
