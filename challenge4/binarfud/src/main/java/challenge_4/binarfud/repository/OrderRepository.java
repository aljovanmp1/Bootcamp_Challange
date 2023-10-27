package challenge_4.binarfud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import challenge_4.binarfud.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>  {
    
}
