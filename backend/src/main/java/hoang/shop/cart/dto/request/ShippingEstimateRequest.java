package hoang.shop.cart.dto.request;

public record ShippingEstimateRequest(
        Long addressId,
        String shippingMethod
) {

}
