package binarfud.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import binarfud.user.model.ERole;
import binarfud.user.model.Role;
import binarfud.user.repository.RoleRepositry;

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
