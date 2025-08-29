package com.library.service;

import com.library.model.BorrowRecord;
import com.library.model.Fine;
import com.library.model.Member;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.FineRepository;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FineService {
    
    @Autowired
    private FineRepository fineRepository;
    
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    // Rate per day for overdue books (e.g., $1 per day)
    private static final BigDecimal RATE_PER_DAY = new BigDecimal("1.00");
    
    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }
    
    public Optional<Fine> getFineById(Integer id) {
        return fineRepository.findById(id);
    }
    
    public List<Fine> getFinesByBorrowRecord(Integer issueId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(issueId)
            .orElseThrow(() -> new IllegalArgumentException("Borrow record not found with id " + issueId));
        return fineRepository.findByBorrowRecord(borrowRecord);
    }
    
    public List<Fine> getFinesByMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id " + memberId));
        return fineRepository.findByMember(member);
    }
    
    public List<Fine> getUnpaidFines() {
        return fineRepository.findByPaid(Fine.PaymentStatus.No);
    }
    
    public Fine createFine(BorrowRecord borrowRecord, long daysOverdue) {
        BigDecimal amount = RATE_PER_DAY.multiply(new BigDecimal(daysOverdue));
        
        Fine fine = new Fine();
        fine.setBorrowRecord(borrowRecord);
        fine.setAmount(amount);
        fine.setPaid(Fine.PaymentStatus.No);
        
        return fineRepository.save(fine);
    }
    
    public Fine payFine(Integer fineId) {
        Fine fine = fineRepository.findById(fineId)
            .orElseThrow(() -> new IllegalArgumentException("Fine not found with id " + fineId));
        
        if (fine.getPaid() == Fine.PaymentStatus.Yes) {
            throw new IllegalStateException("Fine has already been paid");
        }
        
        fine.setPaid(Fine.PaymentStatus.Yes);
        return fineRepository.save(fine);
    }
    
    public BigDecimal calculateTotalFinesForMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id " + memberId));
        
        List<Fine> unpaidFines = fineRepository.findByMemberAndPaidStatus(member, Fine.PaymentStatus.No);
        
        return unpaidFines.stream()
            .map(Fine::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
