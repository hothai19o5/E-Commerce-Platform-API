package learn.ecommerceplatformapi.service

import jakarta.transaction.Transactional
import learn.ecommerceplatformapi.dto.request.ProductRequest
import learn.ecommerceplatformapi.entity.Product
import learn.ecommerceplatformapi.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class ProductService {

    @Autowired private ProductRepository productRepository

    Page<Product> getProducts(Integer page, Integer limit, String search, String category, String sortBy) {
        // Handle pagination defaults
        if (page == null || page < 1) page = 1
        if (limit == null || limit < 1) limit = 10

        // Handle sorting defaults
        def sort = Sort.unsorted()
        switch (sortBy) {
            case "price_asc":
                sort = Sort.by("price").ascending()
                break
            case "price_desc":
                sort = Sort.by("price").descending()
                break
            case "rating_asc":
                sort = Sort.by("rating").ascending()
                break
            case "rating_desc":
                sort = Sort.by("rating").descending()
                break
            case "date_asc":
                sort = Sort.by("createdAt").ascending()
                break
            case "date_desc":
                sort = Sort.by("createdAt").descending()
                break
            case "name_asc":
                sort = Sort.by("name").ascending()
                break
            case "name_desc":
                sort = Sort.by("name").descending()
                break
        }

        def pageable = PageRequest.of(page - 1, limit, sort)
        return productRepository.searchProducts(search, category, pageable)
    }

    Product getProductById(Long id) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Invalid product ID")
        return productRepository.findById(id).orElseThrow { new NoSuchElementException("Product not found with id: $id") }
    }

    @Transactional
    Product createProduct(ProductRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Product request cannot be null")
        if (!request.name || !request.price || !request.category || !request.stockQuantity)
            throw new IllegalArgumentException("Missing required product fields")
        if (request.price < 0)
            throw new IllegalArgumentException("Price cannot be negative")
        if (request.stockQuantity < 0)
            throw new IllegalArgumentException("Stock quantity cannot be negative")

        // Save image to cloud
        String imageUrl = ""

        Product product = Product.builder()
                .name(request.name)
                .description(request.description)
                .price(request.price)
                .category(request.category)
                .stockQuantity(request.stockQuantity)
                .imageUrl(imageUrl)
                .build()
        return productRepository.save(product)
    }

    @Transactional
    Product updateProduct(Long id, Map<String, Object> updates) {
        Product product = getProductById(id)

        updates.each { key, value ->
            switch (key) {
                case "name":
                    product.name = value as String
                    break
                case "description":
                    product.description = value as String
                    break
                case "price":
                    Double price = value as Double
                    if (price < 0) {
                        throw new IllegalArgumentException("Price cannot be negative")
                    }
                    product.price = price
                    break
                case "category":
                    product.category = value as String
                    break
                case "stockQuantity":
                    Integer stockQuantity = value as Integer
                    if (stockQuantity < 0) {
                        throw new IllegalArgumentException("Stock quantity cannot be negative")
                    }
                    product.stockQuantity = stockQuantity
                    break
                case "imageUrl":
                    product.imageUrl = value as String
                    break
                default:
                    break
            }
        }
        product.updatedAt = LocalDateTime.now()
        return productRepository.save(product)
    }

    @Transactional
    boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow { new NoSuchElementException("Product not found") }
        productRepository.delete(product)
        return true
    }

    @Transactional
    Product updateInventory(Long id, Map<String, Object> request) {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("Invalid product ID")
        if (!request.containsKey("stockQuantity"))
            throw new IllegalArgumentException("Missing stockQuantity in request")

        Integer stockQuantity = request.get("stockQuantity") as Integer
        if (stockQuantity < 0)
            throw new IllegalArgumentException("Stock quantity cannot be negative")

        Product product = getProductById(id)
        switch (request.get("action")) {
            case "set":
                product.stockQuantity = stockQuantity
                break
            case "add":
                product.stockQuantity += stockQuantity
                break
            case "subtract":
                if (product.stockQuantity - stockQuantity < 0)
                    throw new IllegalArgumentException("Stock quantity cannot be negative")
                product.stockQuantity -= stockQuantity
                break
            default:
                throw new IllegalArgumentException("Invalid action. Must be one of: set, add, subtract")
        }
        product.updatedAt = LocalDateTime.now()
        return productRepository.save(product)
    }
}
