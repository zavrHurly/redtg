package com.example.red2.service;

import com.example.red2.models.Book;
import com.example.red2.models.User;
import com.example.red2.repository.BookRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book update(Book book, Long id) {
        Book bookFromDB = bookRepository.getBookByUserId(id);
        BeanUtils.copyProperties(book, bookFromDB, "id");
        return book;
    }

    public Book save(Book book) {
        bookRepository.save(book);
        return book;
    }
}
