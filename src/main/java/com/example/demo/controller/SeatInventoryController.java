package com.example.demo.controller;

import com.example.demo.model.SeatInventoryRecord;
import com.example.demo.service.SeatInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Seat Inventory", description = "Seat inventory management endpoints")
public class SeatInventoryController {
    
    private final SeatInventoryService seatInventoryService;
    
    public SeatInventoryController(SeatInventoryService seatInventoryService) {
        this.seatInventoryService = seatInventoryService;
    }
    
    @PostMapping
    @Operation(summary = "Create seat inventory")
    public ResponseEntity<SeatInventoryRecord> createInventory(@RequestBody SeatInventoryRecord inventory) {
        return ResponseEntity.ok(seatInventoryService.createInventory(inventory));
    }
    
    @PutMapping("/{eventId}/remaining")
    @Operation(summary = "Update remaining seats")
    public ResponseEntity<SeatInventoryRecord> updateRemainingSeats(
            @PathVariable Long eventId, 
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(seatInventoryService.updateRemainingSeats(eventId, body.get("remainingSeats")));
    }
    
    @GetMapping("/event/{eventId}")
    @Operation(summary = "Get inventory by event ID")
    public ResponseEntity<SeatInventoryRecord> getInventoryByEvent(@PathVariable Long eventId) {
        // ✅ FIXED: Service returns SeatInventoryRecord directly, not Optional
        return ResponseEntity.ok(seatInventoryService.getInventoryByEvent(eventId));
    }
    
    @GetMapping
    @Operation(summary = "Get all inventories")
    public ResponseEntity<List<SeatInventoryRecord>> getAllInventories() {
        return ResponseEntity.ok(seatInventoryService.getAllInventories());
    }
}
