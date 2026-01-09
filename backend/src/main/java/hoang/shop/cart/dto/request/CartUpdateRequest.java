package hoang.shop.cart.dto.request;

import hoang.shop.common.enums.status.CartStatus;

public record CartUpdateRequest(
        CartStatus status
) {
}
