package com.library.service;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowRecordService {
    
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private FineService fineService;
    
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }
    
    public Optional<BorrowRecord> getBorrowRecordById(Integer id) {
        return borrowRecordRepository.findById(id);
    }
    
    public List<BorrowRecord> getBorrowRecordsByMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id " + memberId));
        return borrowRecordRepository.findByMember(member);
    }
    
    public List<BorrowRecord> getBorrowRecordsByBook(Integer bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id " + bookId));
        return borrowRecordRepository.findByBook(book);
    }
    
    public List<BorrowRecord> getOverdueBooks() {
        return borrowRecordRepository.findOverdueBooks(LocalDate.now());
    }
    
    @Transactional
    public BorrowRecord issueBook(Integer memberId, Integer bookId, Integer durationDays) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id " + memberId));
        
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id " + bookId));
        
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for book: " + book.getTitle());
        }
        
        // Create new borrow record
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setMember(member);
        borrowRecord.setBook(book);
        borrowRecord.setIssueDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(durationDays));
        borrowRecord.setStatus(BorrowRecord.BorrowStatus.Issued);
        
        // Update book available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        
        return borrowRecordRepository.save(borrowRecord);
    }
    
    @Transactional
    public BorrowRecord returnBook(Integer issueId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(issueId)
            .orElseThrow(() -> new IllegalArgumentException("Borrow record not found with id " + issueId));
        
        if (borrowRecord.getStatus() == BorrowRecord.BorrowStatus.Returned) {
            throw new IllegalStateException("Book has already been returned");
        }
        
        // Set return date and update status
        borrowRecord.setReturnDate(LocalDate.now());
        
        // Check if book is overdue
        if (LocalDate.now().isAfter(borrowRecord.getDueDate())) {
            borrowRecord.setStatus(BorrowRecord.BorrowStatus.Overdue);
            
            // Calculate fine (e.g., $1 per day overdue)
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(
                borrowRecord.getDueDate(), LocalDate.now());
            
            if (daysOverdue > 0) {
                fineService.createFine(borrowRecord, daysOverdue);
            }
        } else {
            borrowRecord.setStatus(BorrowRecord.BorrowStatus.Returned);
        }
        
        // Update book available copies
        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        
        return borrowRecordRepository.save(borrowRecord);
    }
    
    public void updateBorrowStatus() {
        // Find all issued books that are overdue
        List<BorrowRecord> overdueRecords = borrowRecordRepository.findOverdueBooks(LocalDate.now());
        
        for (BorrowRecord record : overdueRecords) {
            record.setStatus(BorrowRecord.BorrowStatus.Overdue);
            borrowRecordRepository.save(record);
        }
    }
}
