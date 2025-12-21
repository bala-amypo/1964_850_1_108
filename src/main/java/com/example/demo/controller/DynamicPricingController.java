package com.example.demo.controller;

import com.example.demo.model.DynamicPriceRecord;
import com.example.demo.service.DynamicPricingEngineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dynamic-pricing")
@Tag(name = "Dynamic Pricing APIs")
public class DynamicPricingController {

    private final DynamicPricingEngineService service;

    public DynamicPricingController(DynamicPricingEngineService service) {
        this.service = service;
    }

    @PostMapping("/compute/{eventId}")
    public DynamicPriceRecord compute(@PathVariable Long eventId) {
        return service.computeDynamicPrice(eventId);
    }

    @GetMapping("/latest/{eventId}")
    public DynamicPriceRecord latest(@PathVariable Long eventId) {
        return service.getLatestPrice(eventId).orElseThrow();
    }

    @GetMapping("/history/{eventId}")
    public List<DynamicPriceRecord> history(@PathVariable Long eventId) {
        return service.getPriceHistory(eventId);
    }

    @GetMapping
    public List<DynamicPriceRecord> all() {
        return service.getAllComputedPrices();
    }
}
