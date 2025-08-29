package com.library.controller;

import com.library.model.Fine;
import com.library.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fines")
public class FineController {
    
    @Autowired
    private FineService fineService;
    
    @GetMapping
    public ResponseEntity<List<Fine>> getAllFines() {
        return ResponseEntity.ok(fineService.getAllFines());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFineById(@PathVariable Integer id) {
        return fineService.getFineById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/borrow/{issueId}")
    public ResponseEntity<List<Fine>> getFinesByBorrowRecord(@PathVariable Integer issueId) {
        try {
            return ResponseEntity.ok(fineService.getFinesByBorrowRecord(issueId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Fine>> getFinesByMember(@PathVariable Integer memberId) {
        try {
            return ResponseEntity.ok(fineService.getFinesByMember(memberId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/unpaid")
    public ResponseEntity<List<Fine>> getUnpaidFines() {
        return ResponseEntity.ok(fineService.getUnpaidFines());
    }
    
    @PostMapping("/pay/{fineId}")
    public ResponseEntity<?> payFine(@PathVariable Integer fineId) {
        try {
            Fine fine = fineService.payFine(fineId);
            return ResponseEntity.ok(fine);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/total/{memberId}")
    public ResponseEntity<?> getTotalFinesForMember(@PathVariable Integer memberId) {
        try {
            BigDecimal totalFines = fineService.calculateTotalFinesForMember(memberId);
            return ResponseEntity.ok(Map.of("memberId", memberId, "totalFines", totalFines));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
