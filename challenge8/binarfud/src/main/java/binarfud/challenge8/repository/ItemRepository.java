package binarfud.challenge8.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import binarfud.challenge8.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>  {
    
}
