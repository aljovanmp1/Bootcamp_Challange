package challenge6.binarfud.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import challenge6.binarfud.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>  {
    
}
