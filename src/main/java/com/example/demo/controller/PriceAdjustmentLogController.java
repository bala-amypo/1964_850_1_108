package com.example.demo.controller;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.service.PriceAdjustmentLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adjustments")
public class PriceAdjustmentLogController {
    
    private final PriceAdjustmentLogService priceAdjustmentLogService;
    
    public PriceAdjustmentLogController(PriceAdjustmentLogService priceAdjustmentLogService) {
        this.priceAdjustmentLogService = priceAdjustmentLogService;
    }
    
    @GetMapping("/event/{eventId}")
    public List<PriceAdjustmentLog> getAllAdjustments(@PathVariable Long eventId) {
        return priceAdjustmentLogService.getAdjustmentsByEvent(eventId);
    }
    
    public String logAdjustment(PriceAdjustmentLog log) {
        return "Logged";
    }
}
