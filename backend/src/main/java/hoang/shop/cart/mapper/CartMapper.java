package hoang.shop.cart.mapper;

import hoang.shop.cart.dto.request.CartUpdateRequest;
import hoang.shop.cart.dto.response.CartResponse;
import hoang.shop.cart.dto.response.CartSummary;
import hoang.shop.cart.model.Cart;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring",uses = { CartItemMapper.class } )
public interface CartMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(CartUpdateRequest request, @MappingTarget Cart entity);

    @Mapping(target = "taxAmount", expression = "java(calculateTax(entity))")
    CartResponse toResponse(Cart entity);
    default BigDecimal calculateTax(Cart cart){
        BigDecimal total = cart.getGrandTotal();
        return total.multiply(BigDecimal.valueOf(0.1));
    }
}
