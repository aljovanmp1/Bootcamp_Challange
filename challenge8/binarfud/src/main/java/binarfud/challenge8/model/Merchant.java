package binarfud.challenge8.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "merchant")
@Setter
@Getter
@NoArgsConstructor
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "merchant_name")
    private String merchantName;
    @Column(name = "merchant_location")
    private String merchantLocation;
    private Boolean open;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
