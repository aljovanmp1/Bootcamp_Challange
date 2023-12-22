package binarfud.challenge8.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import binarfud.challenge8.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>  {
    Optional<Order> findById(UUID id);
}
