package ru.otus.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dao.BooksRepository;
import ru.otus.library.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BooksRepository repository;

    @Autowired
    public BookService(BooksRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    public List<Book> getBooks() {
        List<Book> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public void addBook(String name) {
        Book book = new Book();
        book.setName(name);
        repository.save(book);
    }

    @Transactional
    public void removeBook(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void reserveBook(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            Book entity = book.get();
            entity.setBooked(true);
            repository.save(entity);
        }
    }

    @Transactional
    public void returnBook(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            Book entity = book.get();
            entity.setBooked(false);
            repository.save(entity);
        }
    }
}
