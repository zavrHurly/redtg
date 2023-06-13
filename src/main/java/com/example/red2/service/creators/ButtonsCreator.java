package com.example.red2.service.creators;

import com.example.red2.repository.BookingRepository;
import com.example.red2.repository.UserRepository;
import com.example.red2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.red2.models.AnswersAndKeyboards.*;
import static com.example.red2.models.AnswersAndKeyboards.CREATE_BOOK_ANSWER;

public class ButtonsCreator {

    private final UserService userService;

    @Autowired
    public ButtonsCreator(UserRepository userRepository) {
        this.userService = new UserService(userRepository);
    }

    public EditMessageText createAButtonResponse(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        messageText.setMessageId((int) update.getCallbackQuery().getMessage().getMessageId());
        if (callbackData.equals(BUTTONS_NAME_DEFAULT)) {
            messageText.setText(CREATE_BOOK_ANSWER);
            bookATable(update);
        } else if (callbackData.equals(BUTTONS_NAME_WITH_PS)) {
            messageText.setText(CREATE_BOOK_ANSWER);
            bookATablePS(update);
        }
        return messageText;
    }

    public void bookATable(Update update) {
        userService.setAction(update.getCallbackQuery().getMessage().getChatId(), true);
    }

    public void bookATablePS(Update update) {
        userService.setAction(update.getCallbackQuery().getMessage().getChatId(), true);
    }
}
