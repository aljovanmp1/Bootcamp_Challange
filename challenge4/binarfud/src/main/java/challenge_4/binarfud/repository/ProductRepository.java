package challenge_4.binarfud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import challenge_4.binarfud.model.Product;
import challenge_4.binarfud.model.Merchant;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByMerchantOrderByIdAsc(Merchant merchant);
    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE product_id_seq RESTART WITH 1", nativeQuery = true)
    void resetSeq();

}

