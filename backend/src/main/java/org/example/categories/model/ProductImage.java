package org.example.categories.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.common.baseEntity.BaseEntity;
import org.example.common.enums.status.ProductImageStatus;


@Entity
@Table(name = "product_images")

@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class ProductImage extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id",nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_variants_product_id_product"))
    private Product product;
    @Column(name = "image_url",nullable = false,length = 500)
    private String imageUrl;
    @Column(name = "alt_text")
    private String altText;
    @Column(name = "is_main",nullable = false)
    private boolean isMain = false;

    private Integer width;

    private Integer height;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "size_bytes")
    private Long sizeBytes;
    private String checksum;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductImageStatus status = ProductImageStatus.ACTIVE ;



}
