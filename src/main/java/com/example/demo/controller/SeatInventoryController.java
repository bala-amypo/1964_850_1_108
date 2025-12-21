package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Seat Inventory APIs")
public class SeatInventoryController {

    private final SeatInventoryService service;

    public SeatInventoryController(SeatInventoryService service) {
        this.service = service;
    }

    @PostMapping
    public SeatInventoryRecord create(@RequestBody SeatInventoryRecord inventory) {
        return service.createInventory(inventory);
    }

    @PutMapping("/{eventId}/remaining")
    public SeatInventoryRecord updateRemaining(
            @PathVariable Long eventId,
            @RequestParam Integer remainingSeats) {
        return service.updateRemainingSeats(eventId, remainingSeats);
    }

    @GetMapping("/event/{eventId}")
    public SeatInventoryRecord getByEvent(@PathVariable Long eventId) {
        return service.getInventoryByEvent(eventId).orElseThrow();
    }

    @GetMapping
    public List<SeatInventoryRecord> getAll() {
        return service.getAllInventories();
    }
}
