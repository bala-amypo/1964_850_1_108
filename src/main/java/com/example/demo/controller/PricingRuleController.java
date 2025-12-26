package com.example.demo.controller;

import com.example.demo.model.PricingRule;
import com.example.demo.service.PricingRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class PricingRuleController {
    
    private final PricingRuleService pricingRuleService;
    
    public PricingRuleController(PricingRuleService pricingRuleService) {
        this.pricingRuleService = pricingRuleService;
    }
    
    @PostMapping
    public PricingRule createRule(@RequestBody PricingRule rule) {
        return pricingRuleService.createRule(rule);
    }
    
    @GetMapping
    public List<PricingRule> getAllRules() {
        return pricingRuleService.getAllRules();
    }
    
    @GetMapping("/active")
    public List<PricingRule> getActiveRules() {
        return pricingRuleService.getActiveRules();
    }
    
    @PutMapping("/{id}/status")
    public PricingRule updateRule(@PathVariable Long id, @RequestParam Boolean active) {
        return pricingRuleService.updateRuleStatus(id, active);
    }
}
