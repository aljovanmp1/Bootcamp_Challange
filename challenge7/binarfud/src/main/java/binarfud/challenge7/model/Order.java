package binarfud.challenge7.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Setter
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "destination_order")
    private String destinationOrder;
    
    @Column(name = "user_id")
    UUID userId;
    
    private Boolean completed;
}
