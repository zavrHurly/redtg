package com.example.red2.service.handlers.bookinghandlers;

import com.example.red2.models.to.BookingTO;
import com.example.red2.service.TelegramBot;
import com.example.red2.service.creators.BookingCreator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Component
public class StartDateBookingHandler implements CreatorBookingHandler {

    @Override
    public boolean isProcessed(BookingTO to) {
        if (to != null) return to.getStartDate() == null;
        return false;
    }

    @Override
    public BookingTO processed(Update update, BookingTO bookingTO, TelegramBot executor, long userId, BookingCreator creator) throws TelegramApiException, ParseException {
        bookingTO.setStartDate(createDateVisit(update.getMessage().getText(), userId));
        creator.sendResultMessageToDate(bookingTO.getStartDate(), userId, executor);
        return bookingTO;
    }

    private LocalDate createDateVisit(String message, long userId) throws TelegramApiException, DateTimeParseException {
        LocalDate today = LocalDate.now();
        if(message.equals("Сегодня")) {
            return today;
        } else if(message.equals("Завтра")) {
            return today.plusDays(1);
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate resultDate = LocalDate.parse(message, formatter);
                if(resultDate.isBefore(LocalDate.now())) return null;
                return resultDate;
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }
}
