package hoang.shop.categories.mapper;

import hoang.shop.categories.dto.request.ProductCreateRequest;
import hoang.shop.categories.dto.request.ProductUpdateRequest;
import hoang.shop.categories.dto.response.*;
import hoang.shop.categories.model.*;
import hoang.shop.config.MapStructConfig;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ColorMapper.class,ProductReviewMapper.class}, config = MapStructConfig.class)
public interface ProductMapper {
    @Mapping(target = "brandId", source = "product.brand.id")
    @Mapping(target = "brandName", source = "product.brand.name")
    @Mapping(target = "brandSlug", source = "product.brand.slug")
    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.name")
    @Mapping(target = "categorySlug", source = "product.category.slug")
    @Mapping(target = "averageRating", source = "stats.averageRating")
    @Mapping(target = "reviewCount", source = "stats.reviewCount")
    @Mapping(target = "ratingStats",
            expression = "java(toRatingStats(stats))")
    @Mapping(target = "categoryPath",
            expression = "java(buildCategoryPath(product.getCategory()))")
    @Mapping(target = "colors", source = "product.colors")

    ProductDetailResponse toDetailResponse(Product product,ProductReviewStats stats);

    @Mapping(target = "brandName", source = "product.brand.name")
    @Mapping(target = "brandSlug", source = "product.brand.slug")
    @Mapping(target = "regularPrice", expression = "java(toDefaultRegularPrice(product))")
    @Mapping(target = "salePrice", expression = "java(toDefaultSalePrice(product))")
    @Mapping(target = "averageRating",source = "stats.averageRating")
    @Mapping(target = "reviewCount",source = "stats.reviewCount")
    @Mapping(target = "imageUrl", expression = "java(toDefaultImageUrl(product))")
    @Mapping(target = "createdAt", source = "product.createdAt")
    ProductListItemResponse toListItemResponse(Product product,ProductReviewStats stats);


    @Mapping(target = "brandId", source = "product.brand.id")
    @Mapping(target = "brandName", source = "product.brand.name")
    @Mapping(target = "brandSlug", source = "product.brand.slug")
    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.name")
    @Mapping(target = "categorySlug", source = "product.category.slug")
    @Mapping(target = "imageUrl", expression = "java(toDefaultImageUrl(product))")
    AdminListItemProductResponse toAdminListItemResponse(Product product);

    Product toEntity(ProductCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(ProductUpdateRequest updateRequest,@MappingTarget Product product);

    default List<CategoryResponse> buildCategoryPath(Category category) {
        List<CategoryResponse> result = new ArrayList<>();
        Category current = category;
        while (current != null) {
            result.addFirst(new CategoryResponse(
                    current.getId(),
                    current.getName(),
                    current.getSlug()
            ));
            current = current.getParent();
        }
        return result;
    }
    default ProductRatingStatsResponse toRatingStats(ProductReviewStats stats) {
        if (stats == null) return null;
        return new ProductRatingStatsResponse(
                stats.getRating1(),
                stats.getRating2(),
                stats.getRating3(),
                stats.getRating4(),
                stats.getRating5()
        );
    }

    default String toDefaultImageUrl(Product product) {
        List<ProductColor> colors = product.getColors();
        if (colors == null || colors.isEmpty()) return "/uploads/default/no-image.png";

        ProductColor color = colors.stream()
                .filter(ProductColor::isMain)
                .findFirst()
                .orElse(colors.getFirst());

        List<ProductColorImage> images = color.getImages();
        if (images == null || images.isEmpty()) return "/uploads/default/no-image.png";

        return images.stream()
                .filter(ProductColorImage::isMain)
                .map(ProductColorImage::getImageUrl)
                .findFirst()
                .orElse(images.getFirst().getImageUrl());
    }
    default BigDecimal toDefaultRegularPrice(Product product) {
        if (product == null) {
            return null;
        }
        List<ProductColor> colors = product.getColors();
        if (colors == null || colors.isEmpty()) {
            return null;
        }
        ProductColor targetColor = colors.stream()
                .filter(ProductColor::isMain)
                .findFirst()
                .orElse(colors.getFirst());
        if (targetColor == null) {
            return null;
        }
        var variants = targetColor.getVariants();
        if (variants == null || variants.isEmpty()) {
            return null;
        }
        var firstVariant = variants.getFirst();
        if (firstVariant == null) {
            return null;
        }
        return firstVariant.getRegularPrice().multiply(BigDecimal.valueOf(1.10));
    }
    default BigDecimal toDefaultSalePrice(Product product){
        if (product == null) {
            return null;
        }
        List<ProductColor> colors = product.getColors();
        if (colors == null || colors.isEmpty()) {
            return null;
        }
        ProductColor targetColor = colors.stream()
                .filter(ProductColor::isMain)
                .findFirst()
                .orElse(colors.getFirst());
        if (targetColor == null) {
            return null;
        }
        var variants = targetColor.getVariants();
        if (variants == null || variants.isEmpty()) {
            return null;
        }
        var firstVariant = variants.getFirst();
        if (firstVariant == null) {
            return null;
        }
        return firstVariant.getSalePrice().multiply(BigDecimal.valueOf(1.10));
    }





}
