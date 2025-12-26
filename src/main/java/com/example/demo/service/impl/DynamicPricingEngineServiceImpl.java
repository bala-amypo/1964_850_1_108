package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {
    
    private final EventRecordRepository eventRecordRepository;
    private final SeatInventoryRecordRepository seatInventoryRecordRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final DynamicPriceRecordRepository dynamicPriceRecordRepository;
    private final PriceAdjustmentLogRepository priceAdjustmentLogRepository;
    
    public DynamicPricingEngineServiceImpl(
            EventRecordRepository eventRecordRepository,
            SeatInventoryRecordRepository seatInventoryRecordRepository,
            PricingRuleRepository pricingRuleRepository,
            DynamicPriceRecordRepository dynamicPriceRecordRepository,
            PriceAdjustmentLogRepository priceAdjustmentLogRepository) {
        this.eventRecordRepository = eventRecordRepository;
        this.seatInventoryRecordRepository = seatInventoryRecordRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        this.dynamicPriceRecordRepository = dynamicPriceRecordRepository;
        this.priceAdjustmentLogRepository = priceAdjustmentLogRepository;
    }
    
    @Override
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {
        // 1. Fetch event
        EventRecord event = eventRecordRepository.findById(eventId)
            .orElseThrow(() -> new NotFoundException("Event not found"));
        
        // 2. Check if event is active
        if (!event.getActive()) {
            throw new BadRequestException("Event is not active");
        }
        
        // 3. Fetch seat inventory
        SeatInventoryRecord inventory = seatInventoryRecordRepository.findByEventId(eventId)
            .orElseThrow(() -> new NotFoundException("Seat inventory not found"));
        
        // 4. Fetch active pricing rules
        List<PricingRule> activeRules = pricingRuleRepository.findByActiveTrue();
        
        // 5. Calculate days until event
        long daysUntilEvent = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());
        int remainingSeats = inventory.getRemainingSeats();
        
        // 6. Find highest multiplier from matching rules
        double highestMultiplier = 1.0;
        StringBuilder appliedRules = new StringBuilder();
        
        for (PricingRule rule : activeRules) {
            boolean seatsMatch = remainingSeats >= rule.getMinRemainingSeats() 
                              && remainingSeats <= rule.getMaxRemainingSeats();
            boolean daysMatch = daysUntilEvent <= rule.getDaysBeforeEvent();
            
            if (seatsMatch && daysMatch) {
                if (rule.getPriceMultiplier() > highestMultiplier) {
                    highestMultiplier = rule.getPriceMultiplier();
                }
                if (appliedRules.length() > 0) {
                    appliedRules.append(",");
                }
                appliedRules.append(rule.getRuleCode());
            }
        }
        
        // 7. Calculate final price
        double computedPrice = event.getBasePrice() * highestMultiplier;
        
        // 8. Create and save price record
        DynamicPriceRecord priceRecord = new DynamicPriceRecord();
        priceRecord.setEventId(eventId);
        priceRecord.setComputedPrice(computedPrice);
        priceRecord.setAppliedRuleCodes(appliedRules.toString());
        
        priceRecord = dynamicPriceRecordRepository.save(priceRecord);
        
        // 9. Get previous price and log adjustment if price changed
        Optional<DynamicPriceRecord> previousPriceOpt = 
            dynamicPriceRecordRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);
        
        if (previousPriceOpt.isPresent()) {
            DynamicPriceRecord previousPrice = previousPriceOpt.get();
            // Make sure we're not comparing with the record we just saved
            if (!previousPrice.getId().equals(priceRecord.getId())) {
                double oldPrice = previousPrice.getComputedPrice();
                if (Math.abs(oldPrice - computedPrice) > 0.01) {
                    PriceAdjustmentLog log = new PriceAdjustmentLog();
                    log.setEventId(eventId);
                    log.setOldPrice(oldPrice);
                    log.setNewPrice(computedPrice);
                    log.setReason("Dynamic pricing rules applied");
                    priceAdjustmentLogRepository.save(log);
                }
            }
        }
        
        return priceRecord;
    }
    
    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return dynamicPriceRecordRepository.findByEventIdOrderByComputedAtDesc(eventId);
    }
    
    @Override
    public Optional<DynamicPriceRecord> getLatestPrice(Long eventId) {
        return dynamicPriceRecordRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);
    }
    
    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return dynamicPriceRecordRepository.findAll();
    }
}
