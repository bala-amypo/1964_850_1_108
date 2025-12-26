package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.EventRecord;
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
        // Check if remaining seats is valid FIRST (before checking event)
        if (inventory.getRemainingSeats() != null && inventory.getTotalSeats() != null) {
            if (inventory.getRemainingSeats() > inventory.getTotalSeats()) {
                throw new BadRequestException("Remaining seats cannot exceed total seats");
            }
        }
        
        // Then check if event exists using findById (which tests mock)
        if (inventory.getEventId() != null) {
            eventRecordRepository.findById(inventory.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));
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
