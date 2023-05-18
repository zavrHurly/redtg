package com.example.red2.repository;

import com.example.red2.models.Book;
import com.example.red2.models.User;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
