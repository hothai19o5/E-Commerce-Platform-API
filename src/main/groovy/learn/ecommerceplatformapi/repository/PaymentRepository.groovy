package learn.ecommerceplatformapi.repository

import learn.ecommerceplatformapi.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository extends JpaRepository<Payment, Long> {

}