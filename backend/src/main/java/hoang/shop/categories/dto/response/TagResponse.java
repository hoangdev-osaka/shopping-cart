package hoang.shop.categories.dto.response;

public record TagResponse(
        Long id,
        String name,
        String slug,
        String description
) {
}
