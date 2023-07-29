package com.example.red2.service.handlers.bookinghandlers;

import com.example.red2.models.to.BookingTO;
import com.example.red2.service.TelegramBot;
import com.example.red2.service.creators.BookingCreator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalTime;


@Component
public class StartTimeBookingHandler implements CreatorBookingHandler {
    @Override
    public boolean isProcessed(BookingTO to) {
        if(to != null) return to.getStartTime() == null && to.getStartDate() != null;
        return false;
    }

    @Override
    public BookingTO processed(Update update, BookingTO bookingTO, TelegramBot executor, long userId, BookingCreator creator) throws TelegramApiException, ParseException {
        bookingTO.setStartTime(messageToTime(update.getMessage().getText()));
        creator.sendResultMessageToTime(bookingTO.getStartTime(), userId, executor);
        return bookingTO;
    }

    private LocalTime messageToTime(String message) throws NullPointerException {
        String [] timer = message.split("[^0-9]");
        String time = null;
        try {
            if (Integer.parseInt(timer[0]) > 1 && Integer.parseInt(timer[0]) < 16) return null;
            if(Integer.parseInt(timer[0]) > 23 | Integer.parseInt(timer[1]) > 59) return null;
            if (timer.length == 2) time = (timer[0] + ":" + timer[1] + ":" + "00");
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | DateTimeException e) {
            return null;
        }
        return LocalTime.parse(time);
    }
}
