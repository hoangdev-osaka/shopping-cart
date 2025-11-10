package org.example.categories.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.common.baseEntity.BaseEntity;
import org.example.common.enums.status.ProductVariantImageStatus;

@Entity
@Table(name = "product_variant_images")
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class ProductVariantImage extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_variant_id",nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_variant_images_product"))
    private ProductVariant productVariant;
    @Column(name = "image_url",nullable = false,length = 500)
    private String imageUrl;
    @Column(name = "alt_text")
    private String altText;
    @Column(name = "is_main",nullable = false)
    private boolean isMain = false;

    private Integer width;

    private Integer height;
    @Column(name = "mime_type",length = 100)
    private String mimeType;
    @Column(name = "size_bytes")
    private Long sizeBytes;
    @Column(length = 64)
    private String checksum;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductVariantImageStatus status = ProductVariantImageStatus.ACTIVE;


}
