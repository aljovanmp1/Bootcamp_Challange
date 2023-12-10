package binarfud.user.repository;

import java.util.UUID;
import java.util.Optional;

import binarfud.user.model.ERole;
// import binarfud.user.model.ERole;
import binarfud.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepositry extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByName(ERole role);
    
}
