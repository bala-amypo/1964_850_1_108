package com.example.demo.controller;

import com.example.demo.model.PriceAdjustmentLog;
import com.example.demo.service.PriceAdjustmentLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-adjustments")
@Tag(name = "Price Adjustment APIs")
public class PriceAdjustmentLogController {

    private final PriceAdjustmentLogService service;

    public PriceAdjustmentLogController(PriceAdjustmentLogService service) {
        this.service = service;
    }

    @PostMapping
    public PriceAdjustmentLog create(@RequestBody PriceAdjustmentLog log) {
        return service.logAdjustment(log);
    }

    @GetMapping("/event/{eventId}")
    public List<PriceAdjustmentLog> byEvent(@PathVariable Long eventId) {
        return service.getAdjustmentsByEvent(eventId);
    }

    @GetMapping
    public List<PriceAdjustmentLog> all() {
        return service.getAllAdjustments();
    }
}
