package com.example.demo.repository;

import com.example.demo.model.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    boolean existsByRuleCode(String code);

    List<PricingRule> findByActiveTrue();
}
