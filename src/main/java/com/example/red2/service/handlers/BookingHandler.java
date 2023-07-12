package com.example.red2.service.handlers;

import com.example.red2.models.Booking;
import com.example.red2.models.TO.BookingTO;
import com.example.red2.repository.BookingRepository;
import com.example.red2.repository.UserRepository;
import com.example.red2.service.BookingService;
import com.example.red2.service.DateTimeHelper;
import com.example.red2.service.UserService;
import com.example.red2.service.creators.BookingCreator;
import com.example.red2.service.creators.MessageCreator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static com.example.red2.models.AnswersAndKeyboards.*;

import static com.example.red2.service.DateTimeHelper.convertDateToLocalDateTime;

@Component
@PropertySource("application.properties")
public class BookingHandler extends AbstractHandler {

    @Value("${admin.chat}")
    private String adminChat;

    private final UserService userService;

    private final BookingService bookingService;

    private final HashMap<Long, BookingTO> bookingInProcessing = new HashMap<>();

    private final MessageCreator messageCreator;

    private final DateTimeHelper helper;

    private final BookingCreator creator;


    @Autowired
    public BookingHandler(UserRepository userRepository, BookingRepository bookingRepository) {
        userService = new UserService(userRepository);
        bookingService = new BookingService(bookingRepository);
        messageCreator = new MessageCreator();
        helper = new DateTimeHelper();
        creator = new BookingCreator();
    }

    public boolean isProcessed(Update update) {
        if(update.hasMessage()) return update.getMessage().getText().equals("Обычный стол") || update.getMessage().getText().equals("Стол с PS") ||
                userService.getAction(update.getMessage().getChatId());
        return false;
    }

    public void processed(Update update) throws TelegramApiException, ParseException {
        long userId = update.getMessage().getChatId();
        startBooking(userId);
        BookingTO bookingTO = bookingInProcessing.get(userId);
        if(bookingTO == null) {
            bookingInProcessing.put(userId, new BookingTO(update.getMessage().getChatId(), update.getMessage().getChat().getUserName()));
            executor.execute(messageCreator.createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_1, DATES_KEYBOARD));
        } else if(bookingTO.getStartDate() == null) {
            bookingTO.setStartDate(helper.createDateVisit(update.getMessage().getText(), userId));
            bookingInProcessing.put(userId, bookingTO);
            creator.sendResultMessage(bookingTO.getStartDate(), userId, executor);
        } else if(bookingTO.getStartTime() == null && bookingTO.getStartDate() != null) {
            bookingTO.setStartTime(helper.messageToTime(update.getMessage().getText()));
            bookingInProcessing.put(userId, bookingTO);
            creator.sendResultMessage(bookingTO.getStartTime(), userId, executor);
        } else if(bookingTO.getPerson() == null && bookingTO.getStartTime() != null) {
            bookingTO.setPerson(update.getMessage().getText());
            bookingInProcessing.put(userId, bookingTO);
            creator.sendResultMessage(bookingTO.getPerson(), userId, executor);
        } else if(bookingTO.getDuration() == null && bookingTO.getPerson() != null) {
            bookingTO.setDuration(update.getMessage().getText());
            creator.sendResultMessage(bookingTO.getDuration(), userId, executor);
        }
        if(bookingTO.getDuration() != null) {
            bookingTO.setFinishTime(convertDateToLocalDateTime(bookingTO.getStartDate(), bookingTO.getStartTime().plusHours(bookingTO.getDuration())));
            bookingInProcessing.put(userId, bookingTO);
            creator.createBook(bookingTO, userId, executor, bookingService);
            userService.setAction(userId, false);
            executor.execute(messageCreator.createMessageToAdmin(Long.parseLong(adminChat), new Booking(bookingTO)));
            bookingInProcessing.put(userId, null);
        }
    }

    private void startBooking(long userId) {
        if(!userService.getAction(userId)){
            userService.setAction(userId, true);
        }
    }
}
