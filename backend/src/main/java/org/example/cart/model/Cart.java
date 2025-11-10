package org.example.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.common.baseEntity.BaseEntity;
import org.example.common.enums.status.CartStatus;
import org.example.identity.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "carts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Cart extends BaseEntity {
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "fk_carts_users"))
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "session_id")
    private Long sessionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private CartStatus status;



}
