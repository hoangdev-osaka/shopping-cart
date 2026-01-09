package hoang.shop.cart.dto.request;

import hoang.shop.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
        @NotNull(message = "{error.order.address-id.not-null}")
        Long addressId,
        @NotNull(message = "{error.order.payment-method.not-null}")
        PaymentMethod paymentMethod,
        String note
) {
}
