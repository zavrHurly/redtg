package com.example.red2.service.handlers.bookinghandlers;

import com.example.red2.models.to.BookingTO;
import com.example.red2.service.TelegramBot;
import com.example.red2.service.creators.BookingCreator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;


@Component
public class StartBookingTOHandlers implements CreatorBookingHandler {

    @Override
    public boolean isProcessed(BookingTO to) {
        return to == null;
    }

    @Override
    public BookingTO processed(Update update, BookingTO bookingTO, TelegramBot executor, long userId, BookingCreator creator) throws TelegramApiException, ParseException {
        creator.sendResultMessageToStart(userId, executor);
        return new BookingTO(userId, update.getMessage().getChat().getUserName());
    }
}
