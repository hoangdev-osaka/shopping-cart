package hoang.shop.identity.repository;

import hoang.shop.common.enums.status.AddressStatus;
import hoang.shop.identity.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findAllByUserId(Long userId);

    @Modifying
    @Query("""
            UPDATE Address a
            SET a.status = ACTIVE
            WHERE a.id = :addressId
            """)
    int restoreById(@Param("addressId") Long addressId);

    Optional<Address> findByIdAndUser_IdAndStatus(Long id, Long userId, AddressStatus status);

    @Modifying
    @Query("""
            UPDATE Address a
            SET a.status = 'ACTIVE'
            WHERE a.id = :addressId
              AND a.user.id = :userId
            """)
    int softDeleteByUser(@Param("addressId") Long addressId,
                         @Param("userId") Long userId);

    @Modifying
    @Query("""
            UPDATE Address a
            SET a.status = 'ACTIVE'
            WHERE a.id = :addressId
              AND a.user.id = :userId
            """)
    int restoreByUser(@Param("addressId") Long addressId,
                      @Param("userId") Long userId);

    @Modifying
    @Query("""
            UPDATE Address a
            SET a.isDefault = false
            WHERE a.user.id = :userId
              AND a.status = 'ACTIVE'
            """)
    int unsetDefault(@Param("userId") Long userId);

    @Modifying
    @Query("""
            UPDATE Address a
            SET a.isDefault = true
            WHERE a.user.id = :userId
              AND a.id = :addressId
              AND a.status = 'ACTIVE'
            """)
    int setDefault(@Param("userId") Long userId,
                   @Param("addressId") Long addressId);

    @Query("""
            SELECT a
            FROM Address a
            WHERE a.user.id = :userId
              AND a.isDefault = true
              AND a.status = 'ACTIVE'
            """)
    Optional<Address> findDefaultAddressByUserId(@Param("userId") Long userId);

    Optional<Address> findByIdAndUser_IdAndStatusAndIsDefault(Long id, Long userId, AddressStatus status, boolean isDefault);

    List<Address> findByUser_IdAndStatus(Long userId, AddressStatus status);
}
