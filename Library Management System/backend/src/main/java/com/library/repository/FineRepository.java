package com.library.repository;

import com.library.model.BorrowRecord;
import com.library.model.Fine;
import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Integer> {
    List<Fine> findByBorrowRecord(BorrowRecord borrowRecord);
    List<Fine> findByPaid(Fine.PaymentStatus paid);
    
    @Query("SELECT f FROM Fine f JOIN f.borrowRecord br WHERE br.member = :member")
    List<Fine> findByMember(Member member);
    
    @Query("SELECT f FROM Fine f JOIN f.borrowRecord br WHERE br.member = :member AND f.paid = :paid")
    List<Fine> findByMemberAndPaidStatus(Member member, Fine.PaymentStatus paid);
}
