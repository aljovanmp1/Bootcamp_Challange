package binarfud.challenge3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import binarfud.challenge3.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByMerchant_id(long id);
    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE product_id_seq RESTART WITH 1", nativeQuery = true)
    void resetSeq();
}

