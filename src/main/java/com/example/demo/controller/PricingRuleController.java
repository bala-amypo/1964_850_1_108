package com.example.demo.controller;

import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing-rules")
@Tag(name = "Pricing Rule APIs")
public class PricingRuleController {

    private final PricingRuleService service;

    public PricingRuleController(PricingRuleService service) {
        this.service = service;
    }

    @PostMapping
    public PricingRule create(@RequestBody PricingRule rule) {
        return service.createRule(rule);
    }

    @PutMapping("/{id}")
    public PricingRule update(@PathVariable Long id,
                              @RequestBody PricingRule rule) {
        return service.updateRule(id, rule);
    }

    @GetMapping("/active")
    public List<PricingRule> getActive() {
        return service.getActiveRules();
    }

    @GetMapping
    public List<PricingRule> getAll() {
        return service.getAllRules();
    }
}
