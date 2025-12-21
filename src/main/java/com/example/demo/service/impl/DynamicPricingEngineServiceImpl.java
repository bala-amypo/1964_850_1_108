package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DynamicPricingEngineService;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DynamicPricingEngineServiceImpl implements DynamicPricingEngineService {

    private final EventRecordRepository eventRepository;
    private final SeatInventoryRecordRepository inventoryRepository;
    private final PricingRuleRepository ruleRepository;
    private final DynamicPriceRecordRepository priceRepository;
    private final PriceAdjustmentLogRepository logRepository;

    // REQUIRED constructor signature
    public DynamicPricingEngineServiceImpl(EventRecordRepository eventRepository,
                                           SeatInventoryRecordRepository inventoryRepository,
                                           PricingRuleRepository ruleRepository,
                                           DynamicPriceRecordRepository priceRepository,
                                           PriceAdjustmentLogRepository logRepository) {
        this.eventRepository = eventRepository;
        this.inventoryRepository = inventoryRepository;
        this.ruleRepository = ruleRepository;
        this.priceRepository = priceRepository;
        this.logRepository = logRepository;
    }

    @Override
    public DynamicPriceRecord computeDynamicPrice(Long eventId) {

        EventRecord event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getActive()) {
            throw new BadRequestException("Event is not active");
        }

        SeatInventoryRecord inventory = inventoryRepository.findByEventId(eventId)
                .orElseThrow(() -> new BadRequestException("Seat inventory not found"));

        long daysToEvent = ChronoUnit.DAYS.between(LocalDate.now(), event.getEventDate());

        List<PricingRule> matchedRules = ruleRepository.findByActiveTrue()
                .stream()
                .filter(rule ->
                        inventory.getRemainingSeats() >= rule.getMinRemainingSeats() &&
                        inventory.getRemainingSeats() <= rule.getMaxRemainingSeats() &&
                        daysToEvent <= rule.getDaysBeforeEvent())
                .collect(Collectors.toList());

        double multiplier = matchedRules.stream()
                .mapToDouble(PricingRule::getPriceMultiplier)
                .max()
                .orElse(1.0);

        double computedPrice = event.getBasePrice() * multiplier;

        String appliedRules = matchedRules.stream()
                .map(PricingRule::getRuleCode)
                .collect(Collectors.joining(","));

        DynamicPriceRecord record =
                new DynamicPriceRecord(null, eventId, computedPrice, appliedRules);

        Optional<DynamicPriceRecord> lastPrice =
                priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);

        if (lastPrice.isPresent() &&
                !lastPrice.get().getComputedPrice().equals(computedPrice)) {

            PriceAdjustmentLog log = new PriceAdjustmentLog(
                    null,
                    eventId,
                    lastPrice.get().getComputedPrice(),
                    computedPrice,
                    "Dynamic pricing update"
            );
            logRepository.save(log);
        }

        return priceRepository.save(record);
    }

    @Override
    public List<DynamicPriceRecord> getPriceHistory(Long eventId) {
        return priceRepository.findByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public Optional<DynamicPriceRecord> getLatestPrice(Long eventId) {
        return priceRepository.findFirstByEventIdOrderByComputedAtDesc(eventId);
    }

    @Override
    public List<DynamicPriceRecord> getAllComputedPrices() {
        return priceRepository.findAll();
    }
}
