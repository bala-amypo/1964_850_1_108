package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "pricing_rules",
    uniqueConstraints = @UniqueConstraint(columnNames = "ruleCode")
)
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleCode;
    private String description;
    private Integer minRemainingSeats;
    private Integer maxRemainingSeats;
    private Integer daysBeforeEvent;
    private Double priceMultiplier;
    private Boolean active = true;

    public PricingRule() {}

    public PricingRule(Long id, String ruleCode, String description,
                       Integer minRemainingSeats, Integer maxRemainingSeats,
                       Integer daysBeforeEvent, Double priceMultiplier, Boolean active) {
        this.id = id;
        this.ruleCode = ruleCode;
        this.description = description;
        this.minRemainingSeats = minRemainingSeats;
        this.maxRemainingSeats = maxRemainingSeats;
        this.daysBeforeEvent = daysBeforeEvent;
        this.priceMultiplier = priceMultiplier;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMinRemainingSeats() { return minRemainingSeats; }
    public void setMinRemainingSeats(Integer minRemainingSeats) { this.minRemainingSeats = minRemainingSeats; }

    public Integer getMaxRemainingSeats() { return maxRemainingSeats; }
    public void setMaxRemainingSeats(Integer maxRemainingSeats) { this.maxRemainingSeats = maxRemainingSeats; }

    public Integer getDaysBeforeEvent() { return daysBeforeEvent; }
    public void setDaysBeforeEvent(Integer daysBeforeEvent) { this.daysBeforeEvent = daysBeforeEvent; }

    public Double getPriceMultiplier() { return priceMultiplier; }
    public void setPriceMultiplier(Double priceMultiplier) { this.priceMultiplier = priceMultiplier; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
