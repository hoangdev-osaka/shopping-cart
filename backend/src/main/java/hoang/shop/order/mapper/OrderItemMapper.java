package hoang.shop.order.mapper;

import hoang.shop.categories.model.Product;
import hoang.shop.categories.model.ProductColor;
import hoang.shop.categories.model.ProductColorImage;
import hoang.shop.categories.model.ProductVariant;
import hoang.shop.config.MapStructConfig;
import hoang.shop.order.dto.request.OderItemUpdateRequest;
import hoang.shop.order.dto.request.OrderItemCreateRequest;
import hoang.shop.order.dto.response.OrderItemResponse;
import hoang.shop.order.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", config = MapStructConfig.class)

public interface OrderItemMapper {
    OrderItem toEntity(OrderItemCreateRequest request);

    void merger(@MappingTarget OrderItem entity, OderItemUpdateRequest request);

    @Mapping(target = "productVariantId", source = "entity.productVariant.id")
    @Mapping(target = "colorName", source = "entity.productVariant.color.name")
    @Mapping(target = "colorHex", source = "entity.productVariant.color.hex")
    @Mapping(target = "sizeName", source = "entity.productVariant.size")
    @Mapping(target = "sku", source = "entity.sku")
    @Mapping(target = "productName", source = "entity.productVariant.color.product.name")
    @Mapping(target = "imageUrl", expression = "java(toDefaultImageUrl(entity))")
    OrderItemResponse toResponse(OrderItem entity);

    default String toDefaultImageUrl(OrderItem orderItem) {
        ProductVariant variant = orderItem.getProductVariant();
        ProductColor color = variant.getColor();
        if (color == null) return "/uploads/default/no-image.png";
        List<ProductColorImage> images = color.getImages();
        return images.stream()
                .filter(ProductColorImage::isMain)
                .map(ProductColorImage::getImageUrl)
                .findFirst()
                .orElse(images.getFirst().getImageUrl());
    }

}
