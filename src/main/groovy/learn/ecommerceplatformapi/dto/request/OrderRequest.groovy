package learn.ecommerceplatformapi.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import learn.ecommerceplatformapi.dto.AddressDTO
import learn.ecommerceplatformapi.entity.EShippingMethod
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class OrderRequest {
    @NotNull
    @Valid
    AddressDTO shippingAddress

    @NotNull
    @Valid
    AddressDTO billingAddress

    @NotNull
    @NotBlank
    @Valid
    String paymentMethodId

    @NotNull
    @Pattern(regexp = "STANDARD|EXPRESS", message = "Shipping method must be either standard or express")
    EShippingMethod shippingMethod
}
