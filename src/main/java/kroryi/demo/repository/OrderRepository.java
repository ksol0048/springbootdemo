package kroryi.demo.repository;

import kroryi.demo.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);

    List<Order> findByTotalAmountGreaterThan(BigDecimal totalAmount);

    List<Order> findByOrderDateAfter(LocalDateTime localDateTime);

    List<Order> findByOrderDateBefore(LocalDateTime localDateTime);

    List<Order> findByOrderDateBetween(LocalDateTime localDateTime1, LocalDateTime localDateTime2);
}
