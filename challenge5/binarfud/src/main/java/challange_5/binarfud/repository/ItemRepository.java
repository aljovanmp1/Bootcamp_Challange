package challange_5.binarfud.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import challange_5.binarfud.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>  {
    
}
