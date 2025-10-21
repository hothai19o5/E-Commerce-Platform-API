package learn.ecommerceplatformapi.repository

import learn.ecommerceplatformapi.entity.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p 
        WHERE p.stockQuantity > 0 
            AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
            AND (:category IS NULL OR p.category = :category)
    """)
    Page<Product> searchProducts(
        @Param("search") String search,
        @Param("category") String category,
        Pageable pageable
    )

    @Query("""
        SELECT p FROM Product p 
        JOIN OrderItem oi ON p.id = oi.product.id 
        GROUP BY p.id 
        ORDER BY SUM(oi.quantity) DESC
    """)
    Page<Product> getTopSellingProducts(Pageable pageable)
}