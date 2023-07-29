package com.example.red2.service.creators;

import com.example.red2.models.Booking;
import com.example.red2.models.to.BookingTO;
import com.example.red2.service.BookingService;
import com.example.red2.service.TelegramBot;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static com.example.red2.models.AnswersAndKeyboards.*;

@Component
@Setter
public class BookingCreator {

    private MessageCreator messageCreator;

    public BookingCreator() {
        messageCreator = new MessageCreator();
    }

    public void sendResultMessageToDate(LocalDate startDate, long userId, TelegramBot executor) throws TelegramApiException {
        if(startDate == null){
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_1, DATES_KEYBOARD));
        } else {
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_15, null));
        }
    }

    public void sendResultMessageToStart(long userId, TelegramBot executor) throws TelegramApiException {
        executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_1, DATES_KEYBOARD));
    }

    public void sendResultMessageToTime(LocalTime startTime, long userId, TelegramBot executor) throws TelegramApiException {
        if(startTime == null){
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_15, null));
        } else {
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_2, null));
        }
    }

    public void sendResultMessageToCountPerson(Integer countPerson, long userId, TelegramBot executor) throws TelegramApiException {
        if(countPerson == null){
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_2, null));
        } else {
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_3, HOURS_KEYBOARD));
        }
    }

    public void sendResultMessageToDuration(Integer duration, long userId, TelegramBot executor) throws TelegramApiException {
        if(duration == null) {
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_3, HOURS_KEYBOARD));
        }
    }

    public void sendResultMessageToBooking(TelegramBot executor, BookingTO bookingTO, String adminChat) throws TelegramApiException {
        executor.execute(messageCreator.createMessageToAdmin(Long.parseLong(adminChat), new Booking(bookingTO)));
    }

    public void createBook(BookingTO to, long userId, TelegramBot executor, BookingService service) throws TelegramApiException {
        if(service.save(new Booking(to)) == null) {
            executor.execute(messageCreator.createMessageWithKeyboard(userId, NEGATIVE_TIME_BOOKING_ANSWER, null));
        } else {
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_4, null));
        }
    }
}
