package challenge6.binarfud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challenge6.binarfud.model.ERole;
import challenge6.binarfud.model.Role;
import challenge6.binarfud.repository.RoleRepositry;

@Service
public class RoleService {
    
    @Autowired
    RoleRepositry roleRepositry;

    public List<Role> getRoles() {  
        return roleRepositry.findAll();
    }

    public Optional<Role> getRole(String role) {
        return roleRepositry.findByName(ERole.valueOf(role));
    }
}
