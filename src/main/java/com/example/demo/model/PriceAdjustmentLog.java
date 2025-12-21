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
    public void onChange() {
        this.changedAt = LocalDateTime.now();
    }

    public PriceAdjustmentLog() {}

    public PriceAdjustmentLog(Long id, Long eventId,
                              Double oldPrice, Double newPrice, String reason) {
        this.id = id;
        this.eventId = eventId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Double getOldPrice() { return oldPrice; }
    public void setOldPrice(Double oldPrice) { this.oldPrice = oldPrice; }

    public Double getNewPrice() { return newPrice; }
    public void setNewPrice(Double newPrice) { this.newPrice = newPrice; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getChangedAt() { return changedAt; }
}
