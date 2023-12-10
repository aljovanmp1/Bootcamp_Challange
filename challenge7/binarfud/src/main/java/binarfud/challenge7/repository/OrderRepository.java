package binarfud.challenge7.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import binarfud.challenge7.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>  {
    Optional<Order> findById(UUID id);
}
