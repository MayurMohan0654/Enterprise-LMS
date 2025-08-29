package com.library.repository;

import com.library.model.BorrowRecord;
import com.library.model.Book;
import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    List<BorrowRecord> findByMember(Member member);
    List<BorrowRecord> findByBook(Book book);
    List<BorrowRecord> findByStatus(BorrowRecord.BorrowStatus status);
    
    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate < :today AND br.status = 'Issued'")
    List<BorrowRecord> findOverdueBooks(LocalDate today);
    
    List<BorrowRecord> findByMemberAndStatus(Member member, BorrowRecord.BorrowStatus status);
}
