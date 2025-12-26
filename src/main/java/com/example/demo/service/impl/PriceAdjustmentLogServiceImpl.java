package com.example.demo.service.impl;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.repository.PriceAdjustmentLogRepository;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAdjustmentLogServiceImpl implements PriceAdjustmentLogService {
    
    private final PriceAdjustmentLogRepository priceAdjustmentLogRepository;
    
    public PriceAdjustmentLogServiceImpl(PriceAdjustmentLogRepository priceAdjustmentLogRepository) {
        this.priceAdjustmentLogRepository = priceAdjustmentLogRepository;
    }
    
    @Override
    public PriceAdjustmentLog logAdjustment(PriceAdjustmentLog log) {
        return priceAdjustmentLogRepository.save(log);
    }
    
    @Override
    public List<PriceAdjustmentLog> getAdjustmentsByEvent(Long eventId) {
        return priceAdjustmentLogRepository.findByEventId(eventId);
    }
    
    @Override
    public List<PriceAdjustmentLog> getAllAdjustments() {
        return priceAdjustmentLogRepository.findAll();
    }
}
