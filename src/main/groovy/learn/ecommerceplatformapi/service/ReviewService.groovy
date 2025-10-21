package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.dto.request.ReviewRequest
import learn.ecommerceplatformapi.dto.response.ReviewResponse
import learn.ecommerceplatformapi.entity.Review
import learn.ecommerceplatformapi.repository.OrderRepository
import learn.ecommerceplatformapi.repository.ProductRepository
import learn.ecommerceplatformapi.repository.ReviewRepository
import learn.ecommerceplatformapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class ReviewService {

    @Autowired private ReviewRepository reviewRepository
    @Autowired private ProductRepository productRepository
    @Autowired private OrderRepository orderRepository
    @Autowired private UserRepository userRepository

    @Transactional
    def addReview(UserDetails userDetails, Long productId, ReviewRequest request) {
        def product = productRepository.findById(productId)
                .orElseThrow { new IllegalArgumentException("Product not found") }

        def user = userRepository.findByUsername(userDetails.username)
                .orElseThrow { new IllegalArgumentException("User not found") }

        boolean hasBought = orderRepository.existsByUserAndProductAndStatus(user, productId, "PAID")
        if (!hasBought) throw new IllegalArgumentException("User has not purchased this product")

        boolean alreadyReviewed = reviewRepository.findByUserAndProduct(user, product)
        if (alreadyReviewed) throw new IllegalArgumentException("User has already reviewed this product")

        Review review = Review.builder()
                .product(product)
                .rating(request.rating)
                .comment(request.comment)
                .user(user)
                .build()

        reviewRepository.save(review)
        return review
    }

    def getReviewsForProduct(Long productId) {
        Integer totalReviews = reviewRepository.countByProductId(productId)
        if (totalReviews == 0 || totalReviews == null) {
            return [averageRating: 0, reviews: [], totalReviews: 0]
        }
        Double averageRating = reviewRepository.findAverageRatingByProductId(productId)
        List<ReviewResponse> reviews = reviewRepository.findByProductId(productId)
                .collect { review ->
                    ReviewResponse.builder()
                            .reviewId(review.id)
                            .userId(review.user.id)
                            .username(review.user.username)
                            .rating(review.rating)
                            .comment(review.comment)
                            .createdAt(review.createdAt.toString())
                            .build()
                }
        return [averageRating: averageRating, reviews: reviews, totalReviews: totalReviews]
    }
}
