package com.example.red2.service.handlers;

import com.example.red2.models.to.BookingTO;
import com.example.red2.repository.UserRepository;
import com.example.red2.service.UserService;
import com.example.red2.service.creators.BookingCreator;
import com.example.red2.service.handlers.bookinghandlers.CreatorBookingHandler;
import com.example.red2.service.handlers.bookinghandlers.StartBookingTOHandlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;


@Component
public class BookingHandler extends AbstractHandler {



    private final UserService userService;

    private final HashMap<Long, BookingTO> bookingInProcessing = new HashMap<>();


    private final BookingCreator creator;

    private final List<CreatorBookingHandler> handlers;

    private final StartBookingTOHandlers startHandler;


    @Autowired
    public BookingHandler(UserRepository userRepository, List<CreatorBookingHandler> handlers, StartBookingTOHandlers startHandler) {
        userService = new UserService(userRepository);
        creator = new BookingCreator();
        this.handlers = handlers;
        this.startHandler = startHandler;
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
        for (CreatorBookingHandler handler : handlers) {
            if (handler.isProcessed(bookingTO)) {
                try {
                    bookingInProcessing.put(userId, handler.processed(update, bookingTO, executor, userId, creator));
                    break;
                } catch (TelegramApiException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(bookingTO != null) bookingInProcessing.put(userId, finishBooking(userId, bookingTO));
    }


    private void startBooking(long userId) {
        if(!userService.getAction(userId)){
            userService.setAction(userId, true);
        }
    }

    private BookingTO finishBooking(long userId, BookingTO bookingTO) {
        if(bookingTO.getFinishStatus()) {
            userService.setAction(userId, false);
            return null;
        }
        return bookingTO;
    }
}
