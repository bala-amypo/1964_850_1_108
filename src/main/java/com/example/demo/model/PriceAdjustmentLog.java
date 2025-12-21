package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_adjustment_logs")
public class PriceAdjustmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;

    private Double oldPrice;
    private Double newPrice;

    private String reason;

    private LocalDateTime changedAt;

    @PrePersist
    public void prePersist() {
        this.changedAt = LocalDateTime.now();
    }

    public PriceAdjustmentLog() {
    }

    public PriceAdjustmentLog(Long id, Long eventId, Double oldPrice,
                              Double newPrice, String reason,
                              LocalDateTime changedAt) {
        this.id = id;
        this.eventId = eventId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.reason = reason;
        this.changedAt = changedAt;
    }

    // getters and setters
    // ...
}
