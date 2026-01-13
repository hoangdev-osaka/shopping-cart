package hoang.shop.cart.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CartResponse(
        Long id,
        Integer totalQuantity,
        BigDecimal subtotalAmount,
        BigDecimal shippingFee,
        BigDecimal taxAmount,
        BigDecimal grandTotal,
        LocalDate estimatedDeliveryFrom,
        LocalDate estimatedDeliveryTo,
        List<ItemResponse> cartItems

) {
}
