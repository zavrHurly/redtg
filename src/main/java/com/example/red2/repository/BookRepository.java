package com.example.red2.repository;

import com.example.red2.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.userId = :id")
    Book getBookByUserId(Long id);

    @Query("SELECT b.startTime FROM Book b WHERE b.userId = :id")
    LocalDateTime getBookStartTime(Long id);

    @Query("SELECT b.person FROM Book b WHERE b.userId = :id")
    Integer getCountPersonByUserId(Long id);

    @Query("SELECT b.finishTime FROM Book b WHERE b.userId = :id")
    LocalDateTime getFinishTimeByUserId(Long id);

}
