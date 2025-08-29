package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }
    
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }
    
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryIgnoreCase(category);
    }
    
    public Book addBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        // Set available copies equal to total copies for new books
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        
        return bookRepository.save(book);
    }
    
    public Book updateBook(Integer id, Book bookDetails) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id " + id));
        
        // Check if ISBN is changed and not already in use
        if (!book.getIsbn().equals(bookDetails.getIsbn())) {
            if (bookRepository.existsByIsbn(bookDetails.getIsbn())) {
                throw new IllegalArgumentException("Book with ISBN " + bookDetails.getIsbn() + " already exists");
            }
        }
        
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublisher(bookDetails.getPublisher());
        book.setIsbn(bookDetails.getIsbn());
        book.setCategory(bookDetails.getCategory());
        
        // Handle changes in copies
        Integer oldTotal = book.getTotalCopies();
        Integer newTotal = bookDetails.getTotalCopies();
        
        if (!oldTotal.equals(newTotal)) {
            // Calculate the difference between available and total
            int difference = book.getTotalCopies() - book.getAvailableCopies();
            book.setTotalCopies(newTotal);
            book.setAvailableCopies(Math.max(0, newTotal - difference));
        }
        
        return bookRepository.save(book);
    }
    
    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found with id " + id));
        bookRepository.delete(book);
    }
}
