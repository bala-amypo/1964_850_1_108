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
import java.util.stream.Collectors;

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
        EventRecord event = eventRecordRepository.findById(eventId)
            .orElseThrow(() -> new NotFoundException("Event not found"));
        
        if (!event.getActive()) {
            throw new BadRequestException("Event is not active");
        }
        
        SeatInventoryRecord inventory = seatInventoryRecordRepository.findByEventId(eventId)
            .orElseThrow(() -> new NotFoundException("Seat inventory not found"));
        
        List<PricingRule> activeRules = pricingRuleRepository.findByActiveTrue();
        
        long daysUntilEvent = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());
        int remainingSeats = inventory.getRemainingSeats();
        
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
        
        double computedPrice = event.getBasePrice() * highestMultiplier;
        
        DynamicPriceRecord priceRecord = new DynamicPriceRecord();
        priceRecord.setEventId(eventId);
        priceRecord.setComputedPrice(computedPrice);
        priceRecord.setAppliedRuleCodes(appliedRules.toString());
        
        priceRecord = dynamicPriceRecordRepository.save(priceRecord);
        
        Optional<DynamicPriceRecord> previousPrice = 
            dynamicPriceRecordRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);
        
        if (previousPrice.isPresent() && !previousPrice.get().getId().equals(priceRecord.getId())) {
            double oldPrice = previousPrice.get().getComputedPrice();
            if (Math.abs(oldPrice - computedPrice) > 0.01) {
                PriceAdjustmentLog log = new PriceAdjustmentLog();
                log.setEventId(eventId);
                log.setOldPrice(oldPrice);
                log.setNewPrice(computedPrice);
                log.setReason("Dynamic pricing rules applied");
                priceAdjustmentLogRepository.save(log);
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
