package learn.ecommerceplatformapi.repository

import io.lettuce.core.dynamic.annotation.Param
import learn.ecommerceplatformapi.entity.EStatusOrder
import learn.ecommerceplatformapi.entity.Order
import learn.ecommerceplatformapi.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserUsername(String username)

    @Query("""
        SELECT COUNT(oi) > 0
        FROM Order o
        JOIN o.items oi
        WHERE o.user = :user
          AND oi.product.id = :productId
          AND o.payment.status = :status
    """)
    boolean existsByUserAndProductAndStatus(
            @Param("user") User user,
            @Param("productId") Long productId,
            @Param("status") String status
    )

    @Query("""
        SELECT SUM(o.total) FROM Order o
        WHERE o.payment.status = 'COMPLETED'
    """)
    Double calculateTotalSales()

    @Query("""
        SELECT FUNCTION('MONTH', o.paidAt) AS month,
               SUM(o.total) AS revenue
        FROM Order o
        WHERE o.payment.status = 'COMPLETED'
        GROUP BY FUNCTION('MONTH', o.paidAt)
        ORDER BY FUNCTION('MONTH', o.paidAt)
    """)
    def getRevenueByMonth()

    @Query("""
        SELECT o FROM Order o
        WHERE (:statusOrder IS NULL OR o.status = :statusOrder)
        ORDER BY 
            CASE WHEN :sortBy = 'createdAt' THEN o.createdAt END DESC,
            CASE WHEN :sortBy = 'total' THEN o.total END DESC
    """)
    List<Order> findByStatusAndSort(
            @Param("statusOrder") EStatusOrder statusOrder,
            @Param("sortBy") String sortBy
    )

    @Modifying
    @Query("""
        UPDATE Order o
        SET o.status = :status
        WHERE o.id = :orderId
    """)
    int updateOrderStatus(
            @Param("orderId") Long orderId,
            @Param("status") EStatusOrder status
    )
}