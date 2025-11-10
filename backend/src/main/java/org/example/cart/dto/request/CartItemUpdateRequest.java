package org.example.cart.dto.request;

import java.math.BigDecimal;

public record CartItemUpdateRequest (
        Long cartId,
        Long productVariantId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineDiscount
){
}
