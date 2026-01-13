package hoang.shop.cart.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ShippingEstimateResponse(
        LocalDate estimatedDeliveryFrom,
        LocalDate estimatedDeliveryTo
) {
}
