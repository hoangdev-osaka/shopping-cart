package hoang.shop.cart.repository;

import hoang.shop.cart.model.Cart;
import hoang.shop.common.enums.status.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE Cart c
            SET c.status = :toStatus
            WHERE c.status = :fromStatus
                AND c.lastActivityAt < :threshold
            """)
    int markAbandoned(@Param("fromStatus") CartStatus fromStatus,
                      @Param("toStatus") CartStatus toStatus,
                      @Param("threshold") Instant threshold);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            DELETE FROM Cart c
            WHERE c.status = :status
                AND c.lastActivityAt < :threshold
            """)
    int deleteByStatusAndLastActivityAtBefore(@Param("status") CartStatus status,
                                              @Param("threshold") Instant threshold);
}
