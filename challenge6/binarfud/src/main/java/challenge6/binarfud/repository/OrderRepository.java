package challenge6.binarfud.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import challenge6.binarfud.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID>  {
    Optional<Order> findById(UUID id);
}
