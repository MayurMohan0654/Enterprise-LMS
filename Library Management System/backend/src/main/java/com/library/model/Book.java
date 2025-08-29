package com.library.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "author", length = 100)
    private String author;
    
    @Column(name = "publisher", length = 100)
    private String publisher;
    
    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;
    
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies;
    
    @Column(name = "available_copies", nullable = false)
    private Integer availableCopies;
    
    @OneToMany(mappedBy = "book")
    private List<BorrowRecord> borrowRecords;
}
