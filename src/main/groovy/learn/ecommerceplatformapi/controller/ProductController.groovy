package learn.ecommerceplatformapi.controller

import learn.ecommerceplatformapi.dto.request.ProductRequest
import learn.ecommerceplatformapi.entity.Product
import learn.ecommerceplatformapi.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController {

    @Autowired private ProductService productService

    @GetMapping("/products")
    ResponseEntity getProducts(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String sortBy
    ) {
        Page<Product> result = productService.getProducts(page, limit, search, category, sortBy)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/products/{id}")
    ResponseEntity getProduct(@PathVariable Long id) {
        Product result = productService.getProductById(id)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity createProduct(@RequestBody ProductRequest request) {
        Product result = productService.createProduct(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    @PutMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Product result = productService.updateProduct(id, updates)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity deleteProduct(@PathVariable Long id) {
        def result = productService.deleteProduct(id)
        if (!result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/admin/products/{id}/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity updateInventory(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Product result = productService.updateInventory(id, request)
        return ResponseEntity.ok(result)
    }
}
