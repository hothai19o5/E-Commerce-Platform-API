package learn.ecommerceplatformapi.dto.response

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class DashboardResponse {
    Double totalSales
    Long totalOrders
    Long totalProducts
    Long totalCustomers
    List<RevenueByMonth> revenueByMonth
    List<TopProduct> topProducts
}

