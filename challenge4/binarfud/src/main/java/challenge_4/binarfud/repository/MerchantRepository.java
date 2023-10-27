package challenge_4.binarfud.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import challenge_4.binarfud.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Page<Merchant> findByOpenEquals(boolean b, Pageable pageable);
    Page<Merchant> findAll(Pageable pageable);
}
