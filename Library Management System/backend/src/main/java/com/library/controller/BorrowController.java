package com.library.controller;

import com.library.model.BorrowRecord;
import com.library.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {
    
    @Autowired
    private BorrowRecordService borrowRecordService;
    
    @GetMapping
    public ResponseEntity<List<BorrowRecord>> getAllBorrowRecords() {
        return ResponseEntity.ok(borrowRecordService.getAllBorrowRecords());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BorrowRecord> getBorrowRecordById(@PathVariable Integer id) {
        return borrowRecordService.getBorrowRecordById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByMember(@PathVariable Integer memberId) {
        try {
            return ResponseEntity.ok(borrowRecordService.getBorrowRecordsByMember(memberId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByBook(@PathVariable Integer bookId) {
        try {
            return ResponseEntity.ok(borrowRecordService.getBorrowRecordsByBook(bookId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowRecord>> getOverdueBooks() {
        return ResponseEntity.ok(borrowRecordService.getOverdueBooks());
    }
    
    @PostMapping("/issue")
    public ResponseEntity<?> issueBook(
            @RequestParam Integer memberId,
            @RequestParam Integer bookId,
            @RequestParam(defaultValue = "14") Integer durationDays) {
        
        try {
            BorrowRecord borrowRecord = borrowRecordService.issueBook(memberId, bookId, durationDays);
            return new ResponseEntity<>(borrowRecord, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/return/{issueId}")
    public ResponseEntity<?> returnBook(@PathVariable Integer issueId) {
        try {
            BorrowRecord borrowRecord = borrowRecordService.returnBook(issueId);
            return ResponseEntity.ok(borrowRecord);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/update-status")
    public ResponseEntity<?> updateBorrowStatus() {
        borrowRecordService.updateBorrowStatus();
        return ResponseEntity.ok(Map.of("message", "Borrow status updated successfully"));
    }
}
