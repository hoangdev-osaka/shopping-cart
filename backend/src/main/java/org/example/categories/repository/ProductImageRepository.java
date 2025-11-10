package org.example.categories.repository;

import org.example.categories.model.ProductImage;
import org.example.categories.service.ProductImageService;
import org.example.common.enums.status.ProductImageStatus;
import org.example.common.enums.status.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    @Query("""
            SELECT pi
            FROM ProductImage pi
            WHERE pi.product.id = :id
            """)
    Set<ProductImage> findByProductId(@Param("id") Long id);


    @Modifying
    @Query("""
            UPDATE ProductImage pi
            SET pi.status = :status
            WHERE pi.id = :id
            """)
    int updateStatusById(@Param("status") ProductImageStatus status, @Param("id") Long id);

    @Query("""
            SELECT pi
            FROM ProductImage pi
            WHERE :status IS NULL OR pi.status = :status
            """)
    Slice<ProductImage> findByStatus(@Param("status") ProductImageStatus status, Pageable pageable);

}
