package hoang.shop.order.dto.response;

import java.math.BigDecimal;

public record RevenueSummaryResponse(
        BigDecimal currentRevenue,
        BigDecimal previousRevenue,
        BigDecimal changeRate
) {
}
