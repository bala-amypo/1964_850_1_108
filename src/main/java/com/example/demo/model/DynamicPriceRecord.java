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
    public void onCompute() {
        this.computedAt = LocalDateTime.now();
    }

    public DynamicPriceRecord() {}

    public DynamicPriceRecord(Long id, Long eventId,
                              Double computedPrice, String appliedRuleCodes) {
        this.id = id;
        this.eventId = eventId;
        this.computedPrice = computedPrice;
        this.appliedRuleCodes = appliedRuleCodes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Double getComputedPrice() { return computedPrice; }
    public void setComputedPrice(Double computedPrice) { this.computedPrice = computedPrice; }

    public String getAppliedRuleCodes() { return appliedRuleCodes; }
    public void setAppliedRuleCodes(String appliedRuleCodes) { this.appliedRuleCodes = appliedRuleCodes; }

    public LocalDateTime getComputedAt() { return computedAt; }
}
