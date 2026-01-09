package hoang.shop.categories.dto.response;

public record BrandResponse(
        Long id,
        String name,
        String slug,
        String description,
        String logoUrl
) {
}
