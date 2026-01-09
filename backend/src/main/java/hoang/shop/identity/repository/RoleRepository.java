package hoang.shop.identity.repository;

import hoang.shop.common.enums.status.RoleStatus;
import hoang.shop.identity.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
    @Query("""
            SELECT r
            FROM Role r
            WHERE
                :keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                AND :status IS NULL OR r.status = :status
            """)

    Page<Role> searchByKeyword(@Param("keyword") String keyword,@Param("status") RoleStatus status, Pageable pageable);
    Page<Role> findAllByStatus( RoleStatus status ,Pageable pageable);

}
