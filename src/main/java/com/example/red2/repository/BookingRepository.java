package com.example.red2.repository;

import com.example.red2.models.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.userId = :id")
    Booking getBookByUserId(Long id);

    @Query("SELECT b.startTime FROM Booking b WHERE b.userId = :id")
    LocalDateTime getBookStartTime(Long id);

    @Query("SELECT b.person FROM Booking b WHERE b.userId = :id")
    Integer getCountPersonByUserId(Long id);

    @Query("SELECT b.finishTime FROM Booking b WHERE b.userId = :id")
    LocalDateTime getFinishTimeByUserId(Long id);

    @Query("SELECT b FROM Booking b WHERE b.date = :date")
    List<Booking> getAllForToday(LocalDate date);

}
