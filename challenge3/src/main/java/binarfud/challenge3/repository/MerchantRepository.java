package binarfud.challenge3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binarfud.challenge3.model.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}