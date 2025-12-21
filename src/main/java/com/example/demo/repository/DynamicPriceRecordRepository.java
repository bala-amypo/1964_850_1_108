package com.example.demo.repository;

import com.example.demo.model.DynamicPriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DynamicPriceRecordRepository extends JpaRepository<DynamicPriceRecord, Long> {

    List<DynamicPriceRecord> findByEventIdOrderByComputedAtDesc(Long eventId);

    Optional<DynamicPriceRecord> findFirstByEventIdOrderByComputedAtDesc(Long eventId);
}
