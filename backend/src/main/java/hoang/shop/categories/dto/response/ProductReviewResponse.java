package hoang.shop.categories.dto.response;

import java.time.Instant;

public record ProductReviewResponse (
        Long id,
        int rating,
        String title,
        String content,
        String imageUrl,
        String userEmail,
        String userAvatarUrl,
        Instant createdAt,
        Instant updatedAt
){
}
