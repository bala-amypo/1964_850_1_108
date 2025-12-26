package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.PricingRule;
import com.example.demo.repository.PricingRuleRepository;
import com.example.demo.service.PricingRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingRuleServiceImpl implements PricingRuleService {
    
    private final PricingRuleRepository pricingRuleRepository;
    
    public PricingRuleServiceImpl(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
    }
    
    @Override
    public PricingRule createRule(PricingRule rule) {
        if (pricingRuleRepository.existsByRuleCode(rule.getRuleCode())) {
            throw new BadRequestException("Rule code already exists");
        }
        
        if (rule.getPriceMultiplier() == null || rule.getPriceMultiplier() <= 0) {
            throw new BadRequestException("Price multiplier must be > 0");
        }
        
        return pricingRuleRepository.save(rule);
    }
    
    @Override
    public PricingRule updateRule(Long id, PricingRule updatedRule) {
        PricingRule rule = pricingRuleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Rule not found"));
        
        rule.setDescription(updatedRule.getDescription());
        rule.setMinRemainingSeats(updatedRule.getMinRemainingSeats());
        rule.setMaxRemainingSeats(updatedRule.getMaxRemainingSeats());
        rule.setDaysBeforeEvent(updatedRule.getDaysBeforeEvent());
        rule.setPriceMultiplier(updatedRule.getPriceMultiplier());
        rule.setActive(updatedRule.getActive());
        
        return pricingRuleRepository.save(rule);
    }
    
    @Override
    public List<PricingRule> getActiveRules() {
        return pricingRuleRepository.findByActiveTrue();
    }
    
    @Override
    public Optional<PricingRule> getRuleByCode(String ruleCode) {
        return pricingRuleRepository.findAll().stream()
            .filter(rule -> rule.getRuleCode().equals(ruleCode))
            .findFirst();
    }
    
    @Override
    public List<PricingRule> getAllRules() {
        return pricingRuleRepository.findAll();
    }
}
