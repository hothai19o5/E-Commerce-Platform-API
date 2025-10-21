package learn.ecommerceplatformapi.controller

import learn.ecommerceplatformapi.dto.request.ReviewRequest
import learn.ecommerceplatformapi.service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products/{productId}/reviews")
class ReviewController {

    @Autowired private ReviewService reviewService

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity addReview(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long productId,
            @RequestBody ReviewRequest request
    ) {
        def result = reviewService.addReview(user, productId, request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    ResponseEntity getReviews(
            @PathVariable Long productId
    ) {
        def result = reviewService.getReviewsForProduct(productId)
        return ResponseEntity.ok(result)
    }
}
