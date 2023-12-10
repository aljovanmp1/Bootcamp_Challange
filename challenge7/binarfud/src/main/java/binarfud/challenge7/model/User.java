package binarfud.challenge7.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_name")
    private String username;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    private String password;

    private Set<Role> roles = new HashSet<>();

    private UUID merchantId;
}
