package com.example.demo.controller;

import com.example.demo.model.EventRecord;
import com.example.demo.service.EventRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event APIs")
public class EventRecordController {

    private final EventRecordService service;

    public EventRecordController(EventRecordService service) {
        this.service = service;
    }

    @PostMapping
    public EventRecord createEvent(@RequestBody EventRecord event) {
        return service.createEvent(event);
    }

    @GetMapping("/{id}")
    public EventRecord getEvent(@PathVariable Long id) {
        return service.getEventById(id);
    }

    @GetMapping
    public List<EventRecord> getAllEvents() {
        return service.getAllEvents();
    }

    @PutMapping("/{id}/status")
    public EventRecord updateStatus(@PathVariable Long id,
                                    @RequestParam boolean active) {
        return service.updateEventStatus(id, active);
    }

    @GetMapping("/lookup/{code}")
    public EventRecord getByCode(@PathVariable String code) {
        return service.getEventByCode(code).orElseThrow();
    }
}
