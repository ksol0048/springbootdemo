package kroryi.demo.service;

import kroryi.demo.domain.Order;
import kroryi.demo.dto.OrderItemDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {

    Iterable<Order> findAll();
    Optional<Order> findOne(Long id);
    List<Order> getCustomerID(Long customerId);

    List<Order> getTotalAmountGreaterThan(BigDecimal totalAmount);

    List<Order> getOrderDateAfter(LocalDateTime localDateTime);

    List<Order> getOrderDateBefore(LocalDateTime localDateTime);

    List<Order> getOrderDateBetween(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    //추후에 Order은 OrderDTO로 변경해야한다.
    @Transactional
    Order createOrder(Long customerId, List<OrderItemDTO> orderItems);
}
