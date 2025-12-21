package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.EventRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.service.EventRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventRecordServiceImpl implements EventRecordService {

    private final EventRecordRepository eventRepository;

    public EventRecordServiceImpl(EventRecordRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventRecord createEvent(EventRecord event) {

        if (eventRepository.existsByEventCode(event.getEventCode())) {
            throw new BadRequestException("Event code already exists");
        }

        if (event.getBasePrice() == null || event.getBasePrice() <= 0) {
            throw new BadRequestException("Base price must be > 0");
        }

        return eventRepository.save(event);
    }

    @Override
    public EventRecord getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public Optional<EventRecord> getEventByCode(String eventCode) {
        return eventRepository.findByEventCode(eventCode);
    }

    @Override
    public List<EventRecord> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public EventRecord updateEventStatus(Long id, boolean active) {
        EventRecord event = getEventById(id);
        event.setActive(active);
        return eventRepository.save(event);
    }
}
