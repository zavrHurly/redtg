package com.example.red2.service;

import com.example.red2.models.Booking;
import com.example.red2.repository.BookingRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class BookingService {

    private BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking save(Booking booking) {
        List<Booking> bookings = bookingRepository.getAllForToday(booking.getDate());
        int count = 0;
        for(Booking bookingFromDb: bookings) {
            if(booking.getStartTime().isBefore(bookingFromDb.getStartTime()) && booking.getStartTime().isAfter(bookingFromDb.getFinishTime())) count++;
            if (count > 6) return null;
        }
        return bookingRepository.save(booking);
    }
}
