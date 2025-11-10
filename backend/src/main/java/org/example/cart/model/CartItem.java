package org.example.cart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.categories.model.Product;
import org.example.categories.model.ProductVariant;
import org.example.common.baseEntity.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem  extends BaseEntity {
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", foreignKey = @ForeignKey(name = "fk_cart_items_carts"))
    private Cart cart;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id",foreignKey = @ForeignKey(name = "fk_cart_items_product_variants"))
    private ProductVariant productVariant;


    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "line_discount")
    private BigDecimal lineDiscount;
    @Column(name = "line_total")
    private BigDecimal lineTotal;
    public void recalc() {
        BigDecimal gross = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.lineTotal = gross.subtract(lineDiscount).max(BigDecimal.ZERO);
    }

}
