package org.example.cart.dto.request;


import org.example.common.baseEntity.BaseEntity;

import java.math.BigDecimal;

public record CartItemCreateRequest(
        Long cartId,
        Long productVariantId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineDiscount
) {

}
