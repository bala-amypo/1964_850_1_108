package com.example.demo.service;

import com.example.demo.model.PricingRule;
import java.util.List;

public interface PricingRuleService {
    PricingRule createRule(PricingRule rule);
    List<PricingRule> getAllRules();
    List<PricingRule> getActiveRules();
    PricingRule updateRuleStatus(Long id, Boolean active);
}
