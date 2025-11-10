package org.example.categories.repository;

import org.example.categories.dto.request.IdListRequest;
import org.example.categories.model.ProductVariantImage;
import org.example.common.enums.status.ProductVariantImageStatus;
import org.example.common.enums.status.ProductVariantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductVariantImageRepository extends JpaRepository<ProductVariantImage,Long> {
    @Modifying
    @Query("""
            UPDATE ProductVariantImage pvi
            SET pvi.status = :status
            WHERE pvi.id IN :ids
            """)
    int updateStatusById(@Param("status") ProductVariantImageStatus status,@Param("ids") IdListRequest ids);
    boolean existsByProductVariantIdAndIsMainTrue(Long id);
    @Query("""
            SELECT pvi
            FROM ProductVariantImage pvi
            WHERE pvi.productVariant.product.id = :id
                AND (:color IS NULL OR pvi.productVariant.color = :color)
                AND (:size IS NULL OR pvi.productVariant.size = :size)
                AND (:status IS NULL OR pvi.status = :status)
            """)
    Slice<ProductVariantImage> findByProductVariantIdAndColorAndSizeAndStatus(
            @Param("id") Long id,
            @Param("color") String color,
            @Param("size") String size,
            @Param("status")ProductVariantImageStatus status,
            Pageable pageable);

}
