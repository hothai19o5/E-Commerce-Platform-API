package learn.ecommerceplatformapi.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.web.multipart.MultipartFile

class ProductRequest {
    @NotBlank
    String name

    @NotBlank
    String description

    @NotNull
    @Positive
    Double price

    @NotBlank
    String category

    @NotNull
    @PositiveOrZero
    Integer stockQuantity

    MultipartFile image
}
