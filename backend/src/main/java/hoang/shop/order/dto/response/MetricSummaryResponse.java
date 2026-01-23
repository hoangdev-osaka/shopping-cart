package hoang.shop.order.dto.response;

import java.math.BigDecimal;

public record MetricSummaryResponse(
        long current,
        long previous,
        BigDecimal changeRate
) {
}
