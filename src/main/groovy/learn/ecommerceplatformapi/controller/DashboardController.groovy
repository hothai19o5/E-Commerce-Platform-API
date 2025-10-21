package learn.ecommerceplatformapi.controller

import learn.ecommerceplatformapi.entity.EStatusOrder
import learn.ecommerceplatformapi.entity.Order
import learn.ecommerceplatformapi.service.DashboardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin")
class DashboardController {

    @Autowired private DashboardService dashboardService

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity getDashboard() {
        def result = dashboardService.getDashboardData()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity getOrders(
            @RequestParam (required = false) EStatusOrder statusOrder,
            @RequestParam (required = false) String sortBy
    ) {
        List<Order> result = dashboardService.getOrders(statusOrder, sortBy)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/orders/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, EStatusOrder> request
    ) {
        if (!request.containsKey("status")) {
            return ResponseEntity.badRequest().body([message: "Missing 'status' in request body"])
        }
        def result = dashboardService.updateOrderStatus(orderId, request.get("status"))
        return ResponseEntity.ok(result)
    }
}
