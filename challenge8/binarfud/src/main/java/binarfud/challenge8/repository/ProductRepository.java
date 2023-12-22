package binarfud.challenge8.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import binarfud.challenge8.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByMerchantIdAndIsDeleted(UUID merchantId, boolean deleted, Pageable pageable);
    int countByMerchantId(UUID merchantid);
    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE product_id_seq RESTART WITH 1", nativeQuery = true)
    void resetSeq();

}

