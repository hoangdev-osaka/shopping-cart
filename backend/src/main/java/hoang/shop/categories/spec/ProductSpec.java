package hoang.shop.categories.spec;

import hoang.shop.categories.dto.request.ProductSearchCondition;
import hoang.shop.categories.dto.request.PublicProductSearchCondition;
import hoang.shop.categories.model.Category;
import hoang.shop.categories.model.Product;
import hoang.shop.categories.model.ProductColor;
import hoang.shop.categories.model.ProductVariant;
import hoang.shop.common.enums.status.ProductStatus;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProductSpec {
    public static Specification<Product> build(ProductSearchCondition condition) {
        if (condition == null) return null;
        return Specification
                .where(categoryIdEq(condition.categoryId()))
                .and(brandIdEq(condition.brandId()))
                .and(hasTag(condition.tagId()))
                .and(minPrice(condition.minPrice()))
                .and(maxPrice(condition.maxPrice()))
                .and(keywordLike(condition.keyword())
                        .and(statusEq(condition.status())));
    }

    public static Specification<Product> buildPublic(PublicProductSearchCondition condition) {
        if (condition == null) return null;
        return Specification
                .where(hasCategorySlugs(condition.categorySlugs()))
                .and(hasBrandSlugs(condition.brandSlugs()))
                .and(hasTag(condition.tagSlug()))
                .and(hasColors(condition.colors()))
                .and(minPrice(condition.minPrice()))
                .and(maxPrice(condition.maxPrice()))
                .and(keywordLike(condition.keyword())
                        .and(statusEq(ProductStatus.ACTIVE))
                );
    }

    private static Specification<Product> hasColors(List<String> colors) {
        return (root, query, cb) -> {
            if (colors == null || colors.isEmpty()) return null;
            if (query != null) query.distinct(true);

            Join<Product, ProductColor> colorJoin = root.join("colors", JoinType.INNER);
            return colorJoin.get("colorFamily").in(colors);
        };
    }

    private static Specification<Product> hasCategorySlugs(List<String> categorySlugs) {
        return (root, query, cb) -> {
            if (categorySlugs == null || categorySlugs.isEmpty()) return null;
            if (query != null) query.distinct(true);
            return root.get("category").get("slug").in(categorySlugs);
        };
    }

    private static Specification<Product> hasBrandSlugs(List<String> brandSlugs) {
        return (root, query, cb) -> {
            if (brandSlugs == null || brandSlugs.isEmpty()) return null;
            if (query != null) query.distinct(true);
            return root.get("brand").get("slug").in(brandSlugs);
        };
    }

    private static Specification<Product> hasTag(String tagSlug) {
        return (root, query, cb) -> {
            if (tagSlug == null) {
                return null;
            }
            if (query != null) {
                query.distinct(true);
            }
            Join<Object, Object> tagJoin = root.join("tags");
            return cb.equal(tagJoin.get("slug"), tagSlug);
        };
    }

    private static Specification<Product> categoryIdEq(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    private static Specification<Product> brandIdEq(Long brandId) {
        return (root, query, cb) ->
                brandId == null ? null : cb.equal(root.get("brand").get("id"), brandId);
    }

    private static Specification<Product> hasTag(Long tagId) {
        return (root, query, cb) -> {
            if (tagId == null) {
                return null;
            }
            if (query != null) {
                query.distinct(true);
            }
            Join<Object, Object> tagJoin = root.join("tags");
            return cb.equal(tagJoin.get("id"), tagId);
        };
    }

    private static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, cb) -> {
            if (minPrice == null) return null;
            Objects.requireNonNull(query).distinct(true);
            Join<Product, ProductColor> colorJoin = root.join("colors", JoinType.INNER);
            Join<ProductColor, ProductVariant> variantJoin = colorJoin.join("variants", JoinType.INNER);
            Expression<BigDecimal> effectivePrice = cb.<BigDecimal>selectCase()
                    .when(
                            cb.or(
                                    cb.isNull(variantJoin.get("salePrice")),
                                    cb.lessThanOrEqualTo(variantJoin.get("salePrice"), BigDecimal.ZERO)
                            ),
                            variantJoin.get("regularPrice")
                    )
                    .otherwise(variantJoin.get("salePrice"));
            return cb.greaterThanOrEqualTo(effectivePrice, minPrice);
        };
    }

    private static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null) return null;
            Objects.requireNonNull(query).distinct(true);
            Join<Product, ProductColor> colorJoin = root.join("colors", JoinType.INNER);
            Join<ProductColor, ProductVariant> variantJoin = colorJoin.join("variants", JoinType.INNER);
            Expression<BigDecimal> effectivePrice = cb.<BigDecimal>selectCase()
                    .when(
                            cb.or(
                                    cb.isNull(variantJoin.get("salePrice")),
                                    cb.lessThanOrEqualTo(variantJoin.get("salePrice"), BigDecimal.ZERO)
                            ),
                            variantJoin.get("regularPrice")
                    )
                    .otherwise(variantJoin.get("salePrice"));
            return cb.lessThanOrEqualTo(effectivePrice, maxPrice);
        };
    }

    private static Specification<Product> statusEq(ProductStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<Product> keywordLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.trim() + "%";
            return cb.or(
                    cb.like(root.get("name"), pattern),
                    cb.like(root.get("slug"), pattern));
        };
    }


}
