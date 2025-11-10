package org.example.categories.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.common.baseEntity.BaseEntity;
import org.example.common.enums.status.ProductStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_products_name",columnNames = "name")
        },
        indexes = {
                @Index(name = "ix_products_brand_id", columnList = "brand_id"),
                @Index(name = "ix_products_category_id", columnList = "category_id"),
                @Index(name = "ix_products_status", columnList = "status")
        }
)
public class Product extends BaseEntity {
    //constrain

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<ProductTag> productTags = new ArrayList<>();


    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderColumn(name = "sort_order")
    private List<ProductVariant> productVariants = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @OrderColumn(name = "sort_order")
    private List<ProductImage> productImages = new ArrayList<>();


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id",foreignKey = @ForeignKey(name = "fk_products_brand"))
    private Brand brand;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "fk_products_category"))
    private Category category;

    //field
    @Column(nullable = false,length = 255)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private String slug;
    @Column(precision = 15,scale = 2, nullable = false)
    private BigDecimal price;
    @Column(name = "discount_price",precision = 15,scale = 2)
    private BigDecimal discountPrice;
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    @Enumerated(EnumType.STRING)
    @Column( length = 20,nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    public void addVariant(ProductVariant variant){
        if (variant == null) return;
        productVariants.add(variant);
        variant.setProduct(this);
    }
    public void removeVariant(ProductVariant variant){
        if (variant == null) return;
        productVariants.remove(variant);
        variant.setProduct(null);
    }
    public void addImage(ProductImage productImage){
        productImages.add(productImage);
        productImage.setProduct(this);
    }
    public void removeImage(ProductImage productImage){
        productImages.remove(productImage);
        productImage.setProduct(null);
    }

}
