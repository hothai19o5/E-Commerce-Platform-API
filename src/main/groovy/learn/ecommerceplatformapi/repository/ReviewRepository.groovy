package learn.ecommerceplatformapi.repository

import io.lettuce.core.dynamic.annotation.Param
import learn.ecommerceplatformapi.entity.Product
import learn.ecommerceplatformapi.entity.Review
import learn.ecommerceplatformapi.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT SUM(rating) / COUNT(id)
        FROM Review
        WHERE product.id = :productId
    """)
    Double findAverageRatingByProductId(Long productId)

    Integer countByProductId(Long productId)

    List<Review> findByProductId(Long productId)

    @Query("""
        SELECT COUNT(r) > 0
        FROM Review r
        WHERE r.user = :user AND r.product = :product
    """)
    boolean findByUserAndProduct(
            @Param("user") User user,
            @Param("product") Product product
    )
}