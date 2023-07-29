package com.example.red2.service.handlers.bookinghandlers;

import com.example.red2.models.to.BookingTO;
import com.example.red2.repository.BookingRepository;
import com.example.red2.service.BookingService;
import com.example.red2.service.TelegramBot;
import com.example.red2.service.creators.BookingCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;

@Component
@PropertySource("application.properties")
public class FinalistBookingHandler implements CreatorBookingHandler {

    @Value(value = "${admin.chat}")
    private String adminChat;

    private final BookingService bookingService;


    @Autowired
    public FinalistBookingHandler(BookingRepository bookingRepository) {
        bookingService = new BookingService(bookingRepository);
    }


    @Override
    public boolean isProcessed(BookingTO to) {
        if(to != null) return to.getDuration() == null && to.getCountPerson() != null;
        return false;
    }

    @Override
    public BookingTO processed(Update update, BookingTO bookingTO, TelegramBot executor, long userId, BookingCreator creator) throws TelegramApiException, ParseException {
        bookingTO.setDuration(update.getMessage().getText());
        creator.sendResultMessageToDuration(bookingTO.getDuration(), userId, executor);
        bookingTO.setFinishTime(bookingTO.getStartTime().plusHours(bookingTO.getDuration()).atDate(bookingTO.getStartDate()));
        creator.createBook(bookingTO, userId, executor, bookingService);
        creator.sendResultMessageToBooking(executor, bookingTO, adminChat);
        bookingTO.setFinishStatus(true);
        return bookingTO;
    }
}
