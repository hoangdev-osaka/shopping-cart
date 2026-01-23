package hoang.shop.order.service;

import hoang.shop.common.enums.DashboardRange;
import hoang.shop.identity.repository.UserRepository;
import hoang.shop.order.dto.response.DashboardOverviewResponse;
import hoang.shop.order.dto.response.MetricSummaryResponse;
import hoang.shop.order.dto.response.RevenueSummaryResponse;
import hoang.shop.order.repository.OrderItemRepository;
import hoang.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private static final int FROM = 0;
    private static final int TO = 1;
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("Asia/Tokyo");

    @Override
    public DashboardOverviewResponse getOverview(DashboardRange dashboardRange) {
        Instant[] currentRange = resolveRange(dashboardRange);
        Instant from = currentRange[FROM];
        Instant to = currentRange[TO];

        Instant[] previousRange = resolvePreviousRange(dashboardRange, from, to);
        Instant prevFrom = previousRange[FROM];
        Instant prevTo = previousRange[TO];

        BigDecimal currentRevenue = safe(orderRepository.calculateRevenue(from, to));
        BigDecimal previousRevenue = safe(orderRepository.calculateRevenue(prevFrom, prevTo));
        BigDecimal changeRate = calculateChangeRate(currentRevenue, previousRevenue);
        Long currentOrderCount = orderRepository.calculateOrders(from, to);
        Long previousOrderCount = orderRepository.calculateOrders(prevFrom, prevTo);
        BigDecimal orderGrowthRate;

        if (previousOrderCount == 0) {
            orderGrowthRate = BigDecimal.ZERO;
        } else {
            orderGrowthRate = BigDecimal.valueOf(currentOrderCount - previousOrderCount)
                    .divide(BigDecimal.valueOf(previousOrderCount), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        Long currentUserCount = userRepository.calculateUser(from, to);
        Long previousUserCount = userRepository.calculateUser(prevFrom, prevTo);
        BigDecimal userGrowthRate;

        if (previousUserCount == 0) {
            userGrowthRate = BigDecimal.ZERO;
        } else {
            userGrowthRate = BigDecimal.valueOf(currentUserCount - previousUserCount)
                    .divide(BigDecimal.valueOf(previousUserCount), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        RevenueSummaryResponse revenue = new RevenueSummaryResponse(currentRevenue, previousRevenue, changeRate);
        MetricSummaryResponse orderCount = new MetricSummaryResponse(currentOrderCount, previousOrderCount, orderGrowthRate);
        MetricSummaryResponse visitorCount = new MetricSummaryResponse(1, 1, BigDecimal.ZERO);
        MetricSummaryResponse newUserCount = new MetricSummaryResponse(currentUserCount, previousUserCount, userGrowthRate);
        return new DashboardOverviewResponse(revenue, orderCount, visitorCount, newUserCount);
    }

    private Instant[] resolveRange(DashboardRange range) {
        LocalDate today = LocalDate.now(BUSINESS_ZONE);

        Instant to = today.plusDays(1)
                .atStartOfDay(BUSINESS_ZONE)
                .toInstant(); // exclusive

        return switch (range) {
            case LAST_7_DAYS -> new Instant[]{
                    today.minusDays(6).atStartOfDay(BUSINESS_ZONE).toInstant(),
                    to
            };
            case TODAY -> new Instant[]{
                    today.atStartOfDay(BUSINESS_ZONE).toInstant(),
                    to
            };

            case WTD -> new Instant[]{
                    today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                            .atStartOfDay(BUSINESS_ZONE)
                            .toInstant(),
                    to
            };

            case MTD -> new Instant[]{
                    today.withDayOfMonth(1).atStartOfDay(BUSINESS_ZONE).toInstant(),
                    to
            };

            case YTD -> new Instant[]{
                    today.withDayOfYear(1).atStartOfDay(BUSINESS_ZONE).toInstant(),
                    to
            };
        };
    }


    private Instant[] resolvePreviousRange(
            DashboardRange range,
            Instant from,
            Instant to
    ) {
        return switch (range) {

            case LAST_7_DAYS -> {
                Duration d = Duration.between(from, to);
                yield new Instant[]{
                        from.minus(d),
                        from
                };
            }
            case TODAY -> new Instant[]{
                    from.atZone(BUSINESS_ZONE)
                            .minusDays(1)
                            .toInstant(),
                    to.atZone(BUSINESS_ZONE)
                            .minusDays(1)
                            .toInstant()
            };

            case WTD -> new Instant[]{
                    from.minus(7, ChronoUnit.DAYS),
                    to.minus(7, ChronoUnit.DAYS)
            };

            case MTD -> new Instant[]{
                    from.atZone(BUSINESS_ZONE)
                            .minusMonths(1)
                            .toInstant(),
                    to.atZone(BUSINESS_ZONE)
                            .minusMonths(1)
                            .toInstant()
            };

            case YTD -> new Instant[]{
                    from.atZone(BUSINESS_ZONE)
                            .minusYears(1)
                            .toInstant(),
                    to.atZone(BUSINESS_ZONE)
                            .minusYears(1)
                            .toInstant()
            };
        };
    }

    private BigDecimal calculateChangeRate(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return current
                .subtract(previous)
                .divide(previous, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }


}
