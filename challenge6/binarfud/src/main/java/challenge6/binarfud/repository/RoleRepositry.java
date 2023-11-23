package challenge6.binarfud.repository;

import java.util.UUID;
import java.util.Optional;

import challenge6.binarfud.model.ERole;
// import challenge6.binarfud.model.ERole;
import challenge6.binarfud.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositry extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByName(ERole role);
    
}
