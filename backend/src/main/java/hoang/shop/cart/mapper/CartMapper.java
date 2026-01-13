package hoang.shop.cart.mapper;

import hoang.shop.cart.dto.request.CartUpdateRequest;
import hoang.shop.cart.dto.response.CartResponse;
import hoang.shop.cart.dto.response.CartSummary;
import hoang.shop.cart.model.Cart;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring",uses = { CartItemMapper.class } )
public interface CartMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(CartUpdateRequest request, @MappingTarget Cart entity);
    @Mapping(target = "shippingFee", expression = "java(calculateShippingFee(cart))")
    @Mapping(target = "taxAmount", expression = "java(calculateTax(cart))")
    @Mapping(target = "subtotalAmount", expression = "java(calculateSubtotalAmount(cart))")
    CartResponse toResponse(Cart cart);


    default BigDecimal calculateTax(Cart cart){
        BigDecimal total = cart.getGrandTotal();
        return total.multiply(BigDecimal.valueOf(0.1));
    }
    default BigDecimal calculateSubtotalAmount(Cart cart){
        if (cart == null) return BigDecimal.ZERO;
        return cart.getGrandTotal();
    }
    default BigDecimal calculateShippingFee(Cart cart){
        BigDecimal grandTotal = cart.getGrandTotal();
        if (grandTotal.compareTo(BigDecimal.valueOf(20000))>0){
            return BigDecimal.ZERO;
        }else if (grandTotal.compareTo(BigDecimal.valueOf(10000))>0) {
            return BigDecimal.valueOf(200);
        }else
            return BigDecimal.valueOf(500);
    }
}
