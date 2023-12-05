package ru.otus.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.entity.Book;

@Repository
public interface BooksRepository extends CrudRepository<Book, Long> {

}

