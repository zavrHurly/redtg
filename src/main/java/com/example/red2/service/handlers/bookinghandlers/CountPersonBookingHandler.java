package com.example.red2.service.handlers.bookinghandlers;

import com.example.red2.models.to.BookingTO;
import com.example.red2.service.TelegramBot;
import com.example.red2.service.creators.BookingCreator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;

@Component
public class CountPersonBookingHandler implements CreatorBookingHandler {
    @Override
    public boolean isProcessed(BookingTO to) {
        if(to != null) return to.getCountPerson() == null && to.getStartTime() != null;
        return false;
    }

    @Override
    public BookingTO processed(Update update, BookingTO bookingTO, TelegramBot executor, long userId, BookingCreator creator) throws TelegramApiException, ParseException {
        bookingTO.setCountPerson(update.getMessage().getText());
        creator.sendResultMessageToCountPerson(bookingTO.getCountPerson(), userId, executor);
        return bookingTO;
    }
}
