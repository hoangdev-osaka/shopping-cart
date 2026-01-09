package hoang.shop.categories.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record PublicProductSearchCondition(
        List<String> categorySlugs,
        List<String> colors,
        List<String> brandSlugs,
        String tagSlug,
        String keyword,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String sort
) {

}
