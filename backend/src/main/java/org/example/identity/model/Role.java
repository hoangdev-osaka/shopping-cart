package org.example.identity.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.common.baseEntity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles",
        indexes = @Index(name = "ix_roles_name", columnList = "name",unique = true))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Role extends BaseEntity {
        @OneToMany(mappedBy = "role",cascade = CascadeType.ALL,orphanRemoval = true)
        private Set<UserRole> userRoles = new HashSet<>();
        @Column(nullable = false,length = 12)
        private String name;
        @Column(length = 255)
        private String description;
        private boolean deleted = false;


}
