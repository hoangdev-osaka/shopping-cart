package hoang.shop.cart.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long id,
        Integer totalQuantity,
        BigDecimal shippingFee,
        BigDecimal taxAmount,
        BigDecimal grandTotal,
        List<ItemResponse> cartItems

) {
}
