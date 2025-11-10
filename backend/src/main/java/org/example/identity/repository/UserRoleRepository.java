package org.example.identity.repository;

import org.example.identity.model.Role;
import org.example.identity.model.User;
import org.example.identity.model.UserRole;
import org.example.identity.model.UserRoleId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole,UserRoleId> {
    boolean existsByUser_IdAndRole_IdAndDeletedFalse(Long userId, Long roleId);
    int deleteByUser_IdAndRole_Id(Long userId, Long roleId);

    @Query( value = """
                SELECT r
                    FROM UserRole ur
                    JOIN ur.role r
                WHERE ur.user.id = :userId
                    AND ur.deleted = false
                """,
            countQuery = """
                SELECT COUNT(DISTINCT r.id) FROM UserRole ur
                    JOIN ur.role r
                WHERE ur.user.id = :userId AND ur.deleted = false
                """)
    Page<Role> findRolesByUserIdAndDeletedFalse(@Param("userId") Long userId, Pageable pageable);


    @Query( value = """  
                SELECT u
                    FROM UserRole ur
                    JOIN ur.user u
                WHERE ur.role.id = :roleId
                    AND ur.deleted = false
                """,
            countQuery = """
                SELECT COUNT(DISTINCT u.id)
                FROM UserRole ur
                     JOIN ur.user u
                WHERE ur.role.id = :roleId
                    AND ur.deleted = false
                """)
    Page<User> findUsersByRoleIdAndDeletedFalse(@Param("roleId") Long roleId, Pageable pageable);


}
