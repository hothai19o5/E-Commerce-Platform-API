package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.dto.response.DashboardResponse
import learn.ecommerceplatformapi.entity.ERole
import learn.ecommerceplatformapi.entity.EStatusOrder
import learn.ecommerceplatformapi.entity.Order
import learn.ecommerceplatformapi.repository.OrderRepository
import learn.ecommerceplatformapi.repository.ProductRepository
import learn.ecommerceplatformapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DashboardService {

    @Autowired private UserRepository userRepository
    @Autowired private OrderRepository orderRepository
    @Autowired private ProductRepository productRepository

    DashboardResponse getDashboardData() {
        Double totalSales = orderRepository.calculateTotalSales() ?: 0.0
        Long totalOrders = orderRepository.count()
        Long totalProducts = productRepository.count()
        Long totalCustomers = userRepository.countByRole(ERole.ROLE_USER)
        def revenueByMonth = orderRepository.getRevenueByMonth()
        def topProducts = productRepository.getTopSellingProducts(Pageable ofSize(5)).content

        return DashboardResponse.builder()
            .totalSales(totalSales)
            .totalOrders(totalOrders)
            .totalProducts(totalProducts)
            .totalCustomers(totalCustomers)
            .revenueByMonth(revenueByMonth)
            .topProducts(topProducts)
            .build()
    }

    List<Order> getOrders(EStatusOrder statusOrder, String sortBy) {
        List<Order> result = orderRepository.findByStatusAndSort(statusOrder, sortBy)
        return result
    }

    @Transactional
    def updateOrderStatus(Long orderId, EStatusOrder status) {
        int updated = orderRepository.updateOrderStatus(orderId, status)
        if (updated == 0) {
            throw new RuntimeException("Order not found or status not updated")
        }
    }
}
