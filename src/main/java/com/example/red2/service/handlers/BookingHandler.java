package com.example.red2.service.handlers;

import com.example.red2.models.Booking;
import com.example.red2.repository.BookingRepository;
import com.example.red2.repository.UserRepository;
import com.example.red2.service.UserService;
import com.example.red2.service.creators.KeyboardAndInlineCreator;
import com.example.red2.service.creators.MessageCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.red2.models.AnswersAndKeyboards.*;
import static com.example.red2.models.AnswersAndKeyboards.NEGATIVE_ANSWER;

@Component
public class BookingHandler implements Handler {

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private HashMap<Long, Booking> bookingInProcessing = new HashMap<>();

    private MessageCreator messageCreator;

    @Autowired
    public BookingHandler(UserRepository userRepository, BookingRepository bookingRepository) {
        userService = new UserService(userRepository);
        this.bookingRepository = bookingRepository;
        messageCreator = new MessageCreator(new KeyboardAndInlineCreator());
    }

    public boolean isProcessed(Update update) {
        if(update.hasMessage()) {
            return userService.getAction(update.getMessage().getChatId());
        } else {
            return userService.getAction(update.getCallbackQuery().getMessage().getChatId());
        }

    }

    public List<SendMessage> processed(Update update) {
        List<SendMessage> returnList = new ArrayList<>();
        long userId = update.getMessage().getChatId();
        Booking booking = bookingInProcessing.get(userId);
        if(booking == null) {
            bookingInProcessing.put(userId, new Booking(update.getMessage().getChatId(), update.getMessage().getChat().getUserName()));
            messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_1, null);
            return returnList;
        } else if(booking.getStartTime() == null) {
            booking.setStartTime();
            bookingInProcessing.put(userId, booking);
            returnList.add(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_2, null));
            return returnList;
        } else if(booking.getPerson() == null) {
            booking.setPerson(Integer.parseInt(update.getMessage().getText()));
            bookingInProcessing.put(userId, booking);
            returnList.add(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_3, HOURS_KEYBOARD));
            return returnList;
        } else if(booking.getDuration() == null) {
            booking.setDuration(hoursInMessage(update.getMessage().getText()));
            booking.setFinishTime(booking.getStartTime().plusHours(hoursInMessage(update.getMessage().getText())));
            bookingInProcessing.put(userId, booking);
            bookingRepository.save(booking);
            userService.setAction(userId, false);
            returnList.add(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_4, null));
            return returnList;
        }
        returnList.add(messageCreator.createMessageWithKeyboard(userId, NEGATIVE_ANSWER, null));
        return returnList;
    }

    private LocalTime messageToTime(String message) {
        String [] timer = message.split("[^0-9]");
        String dataTime = (timer[0] + ":" + timer[1] + ":" + "00" );
        return LocalTime.parse(dataTime);
    }
}
