package hoang.shop.categories.mapper;

import hoang.shop.categories.dto.request.ProductVariantCreateRequest;
import hoang.shop.categories.dto.request.ProductVariantUpdateRequest;
import hoang.shop.categories.dto.response.AdminVariantResponse;
import hoang.shop.categories.dto.response.VariantResponse;
import hoang.shop.categories.model.ProductVariant;
import hoang.shop.config.MapStructConfig;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring", config = MapStructConfig.class)

public interface VariantMapper {
    @Mapping(target = "regularPrice", source = "variant.regularPrice")
    ProductVariant toEntity(ProductVariantCreateRequest variant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merger(ProductVariantUpdateRequest updateRequest, @MappingTarget ProductVariant productVariant);

    AdminVariantResponse toAdminResponse(ProductVariant productVariant);

    @Mapping(target = "regularPrice", expression = "java(toPriceTaxIncluded(variant.getRegularPrice()))")
    @Mapping(target = "salePrice", expression = "java(toPriceTaxIncluded(variant.getSalePrice()))")
    VariantResponse toResponse(ProductVariant variant);


    default BigDecimal toPriceTaxIncluded(BigDecimal price) {
        if (price == null) return null;
        return price
                .multiply(BigDecimal.valueOf(110))
                .divide(BigDecimal.valueOf(100)); // tax 10%
    }


}
