package hoang.shop.cart.scheduler;

import hoang.shop.cart.service.CartCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CartCleanupScheduler {
    private final CartCleanupService cleanupService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupCarts() {
        cleanupService.markAbandonedCarts();
        cleanupService.deleteOldAbandonedCarts();
    }
}
