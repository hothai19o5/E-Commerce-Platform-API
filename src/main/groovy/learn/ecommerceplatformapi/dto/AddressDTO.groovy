package learn.ecommerceplatformapi.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AddressDTO {
    String fullName
    String stress
    String city
    String state
    String zipCode
    String country
}
