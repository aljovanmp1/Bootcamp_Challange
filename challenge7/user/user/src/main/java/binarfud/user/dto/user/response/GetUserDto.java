package binarfud.user.dto.user.response;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import binarfud.user.model.Role;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserDto {
    private UUID id;

    private String username;
    private String emailAddress;
    
    private Set<Role> roles = new HashSet<>();

    @Column(name = "merchant_id")
    private UUID merchantId;
}
