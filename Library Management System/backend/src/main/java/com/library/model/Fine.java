package com.library.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "fines")
public class Fine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fine_id")
    private Integer fineId;
    
    @ManyToOne
    @JoinColumn(name = "issue_id")
    private BorrowRecord borrowRecord;
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "paid")
    private PaymentStatus paid = PaymentStatus.No;
    
    public enum PaymentStatus {
        Yes, No
    }
}
