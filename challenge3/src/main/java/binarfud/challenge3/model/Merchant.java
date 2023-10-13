package binarfud.challenge3.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_name")
    private String merchantName;
    @Column(name = "merchant_location")
    private String merchantLocation;
    private Boolean open;
}
