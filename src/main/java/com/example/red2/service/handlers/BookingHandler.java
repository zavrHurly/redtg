package com.example.red2.service.handlers;

import com.example.red2.models.Booking;
import com.example.red2.models.TO.BookingTO;
import com.example.red2.repository.BookingRepository;
import com.example.red2.repository.UserRepository;
import com.example.red2.service.DateTimeHelper;
import com.example.red2.service.UserService;
import com.example.red2.service.creators.KeyboardAndInlineCreator;
import com.example.red2.service.creators.MessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static com.example.red2.models.AnswersAndKeyboards.*;
import static com.example.red2.models.AnswersAndKeyboards.NEGATIVE_ANSWER;
import static com.example.red2.service.DateTimeHelper.convertDateToLocalDateTime;

@Component
public class BookingHandler extends AbstractHandler {

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private HashMap<Long, BookingTO> bookingInProcessing = new HashMap<>();

    private MessageCreator messageCreator;

    private DateTimeHelper helper;

    @Autowired
    public BookingHandler(UserRepository userRepository, BookingRepository bookingRepository) {
        userService = new UserService(userRepository);
        this.bookingRepository = bookingRepository;
        messageCreator = new MessageCreator(new KeyboardAndInlineCreator());
        helper = new DateTimeHelper();
    }

    public boolean isProcessed(Update update) {
        if(update.hasMessage()) {
            return userService.getAction(update.getMessage().getChatId());
        } else {
            return userService.getAction(update.getCallbackQuery().getMessage().getChatId());
        }

    }

    public void processed(Update update) throws TelegramApiException {
        long userId = update.getMessage().getChatId();
        BookingTO bookingTO = bookingInProcessing.get(userId);
        if(bookingTO == null) {
            bookingInProcessing.put(userId, new BookingTO(update.getMessage().getChatId(), update.getMessage().getChat().getUserName()));
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_1, DATES_KEYBOARD));
        } else if(bookingTO.getStartDate() == null) {
            bookingTO.setStartDate(createDateVisit(update.getMessage().getText(), userId));
            bookingInProcessing.put(userId, bookingTO);
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_2, null));
        } else if(bookingTO.getStartTime() == null) {
            bookingTO.setStartTime(helper.messageToTime(update.getMessage().getText()));
            bookingInProcessing.put(userId, bookingTO);
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_2, null));
        } else if(bookingTO.getPerson() == null) {
            bookingTO.setPerson(Integer.parseInt(update.getMessage().getText()));
            bookingInProcessing.put(userId, bookingTO);
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_3, HOURS_KEYBOARD));
        } else if(bookingTO.getDuration() == null) {
            bookingTO.setDuration(Long.parseLong(update.getMessage().getText()));
            bookingTO.setFinishTime(convertDateToLocalDateTime(bookingTO.getStartDate(), bookingTO.getStartTime().plusHours(bookingTO.getDuration())));
            bookingInProcessing.put(userId, bookingTO);
            bookingRepository.save(new Booking(bookingTO));
            userService.setAction(userId, false);
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_4, null));
        }
        executor.execute(messageCreator.createMessageWithKeyboard(userId, NEGATIVE_ANSWER, null));
    }

    private Date createDateVisit(String message, long userId) throws TelegramApiException {
        LocalDate today = LocalDate.now();
        if(message.equals("Сегодня")) return Date.from(today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        if(message.equals("Завтра")) return Date.from(today.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        if(message.equals("В другой день")) {
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_4, null));
        }
        return null;
    }
}
