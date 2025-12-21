package com.example.demo.repository;

import com.example.demo.model.EventRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRecordRepository extends JpaRepository<EventRecord, Long> {

    boolean existsByEventCode(String eventCode);

    Optional<EventRecord> findByEventCode(String eventCode);
}