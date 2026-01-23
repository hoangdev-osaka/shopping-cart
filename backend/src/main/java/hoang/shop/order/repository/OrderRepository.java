package hoang.shop.order.repository;

import hoang.shop.common.enums.status.OrderStatus;
import hoang.shop.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByUserIdAndOrderNumber(Long userId, String orderNumber);

    Page<Order> findAllByUser_Id(Long userId, Pageable pageable);

    List<Order> findAllByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

    @Query("""
                SELECT COALESCE(SUM(oi.unitPriceAtOrder * oi.quantity), 0)
                FROM Order o
                JOIN o.items oi
                WHERE o.createdAt >= :from
                  AND o.createdAt <  :to
            """)
    BigDecimal calculateRevenue(@Param("from") Instant from, @Param("to") Instant to);

    @Query("""
              SELECT COUNT(o.id)
              FROM Order o
              WHERE o.createdAt >= :from
                AND o.createdAt <  :to
            """)
    Long calculateOrders(@Param("from") Instant from, @Param("to") Instant to);
}