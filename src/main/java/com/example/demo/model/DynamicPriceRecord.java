package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dynamic_price_records")
public class DynamicPriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;

    private Double computedPrice;

    private String appliedRuleCodes;

    private LocalDateTime computedAt;

    @PrePersist
    public void prePersist() {
        this.computedAt = LocalDateTime.now();
    }

    public DynamicPriceRecord() {
    }

    public DynamicPriceRecord(Long id, Long eventId, Double computedPrice,
                              String appliedRuleCodes, LocalDateTime computedAt) {
        this.id = id;
        this.eventId = eventId;
        this.computedPrice = computedPrice;
        this.appliedRuleCodes = appliedRuleCodes;
        this.computedAt = computedAt;
    }

    // getters and setters
    // ...
}
