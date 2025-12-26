package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.repository.SeatInventoryRecordRepository;
import com.example.demo.service.SeatInventoryService;
import org.springframework.stereotype.Service;

@Service
public class SeatInventoryServiceImpl implements SeatInventoryService {
    
    private final SeatInventoryRecordRepository seatInventoryRecordRepository;
    private final EventRecordRepository eventRecordRepository;
    
    public SeatInventoryServiceImpl(SeatInventoryRecordRepository seatInventoryRecordRepository,
                                   EventRecordRepository eventRecordRepository) {
        this.seatInventoryRecordRepository = seatInventoryRecordRepository;
        this.eventRecordRepository = eventRecordRepository;
    }
    
    @Override
    public SeatInventoryRecord createInventory(SeatInventoryRecord inventory) {
        // CHANGED: Use findById instead of existsById to match test expectations
        eventRecordRepository.findById(inventory.getEventId())
            .orElseThrow(() -> new NotFoundException("Event not found"));
        
        if (inventory.getRemainingSeats() > inventory.getTotalSeats()) {
            throw new BadRequestException("Remaining seats cannot exceed total seats");
        }
        return seatInventoryRecordRepository.save(inventory);
    }
    
    @Override
    public SeatInventoryRecord getInventoryByEvent(Long eventId) {
        return seatInventoryRecordRepository.findByEventId(eventId)
            .orElseThrow(() -> new NotFoundException("Inventory not found"));
    }
    
    @Override
    public SeatInventoryRecord updateInventory(Long id, Integer remainingSeats) {
        SeatInventoryRecord inventory = seatInventoryRecordRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Inventory not found"));
        inventory.setRemainingSeats(remainingSeats);
        return seatInventoryRecordRepository.save(inventory);
    }
}
