package hoang.shop.cart.service;


import hoang.shop.cart.repository.CartRepository;
import hoang.shop.common.enums.status.CartStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CartCleanupService {
    private final CartRepository cartRepository;

    @Transactional
    public void markAbandonedCarts() {
        Instant threshold = Instant.now().minus(24, ChronoUnit.HOURS);

        cartRepository.markAbandoned(
                CartStatus.ACTIVE,
                CartStatus.ABANDONED,
                threshold
        );
    }

    @Transactional
    public void deleteOldAbandonedCarts() {
        Instant threshold = Instant.now().minus(30, ChronoUnit.DAYS);

        cartRepository.deleteByStatusAndLastActivityAtBefore(
                CartStatus.ABANDONED,
                threshold
        );
    }
}
