package com.library.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(name = "phone", length = 15)
    private String phone;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @Column(name = "membership_date")
    private LocalDate membershipDate;
    
    @OneToMany(mappedBy = "member")
    private List<BorrowRecord> borrowRecords;
}
