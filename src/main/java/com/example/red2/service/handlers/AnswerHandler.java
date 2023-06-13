package com.example.red2.service.handlers;

import com.example.red2.repository.BookingRepository;
import com.example.red2.repository.UserRepository;
import com.example.red2.service.UserService;
import com.example.red2.service.creators.KeyboardAndInlineCreator;
import com.example.red2.service.creators.MessageCreator;
import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static com.example.red2.models.AnswersAndKeyboards.*;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AnswerHandler implements Handler {

    private MessageCreator messageCreator;

    private UserService userService;

    @Autowired
    public AnswerHandler(UserRepository userRepository) {
        this.userService = new UserService(userRepository);
        messageCreator = new MessageCreator(new KeyboardAndInlineCreator());
    }

    public boolean isProcessed(Update update) {
        return update.hasMessage() & update.getMessage().hasText() &
                !(update.getMessage().getText().contains("минут") || update.getMessage().getText().contains("час")) &
                !userService.getAction(update.getMessage().getChatId());
    }

    public List<SendMessage> processed(Update update) {
        List<SendMessage> returnList = new ArrayList<>();
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getChat().getFirstName();

        switch (message) {
            case "/start":
                returnList.add(startCommand(chatId, userName));
                return returnList;
            case "/help":
                returnList.add(help(chatId, userName));
            return returnList;
            case "Поставить угли":
                returnList.add(messageCreator.createMessageWithKeyboard(chatId, COAL_ANSWER, COAL_KEYBOARD));
            return returnList;
            case "Забронировать стол":
                returnList.add(messageCreator.createMessageWithMarkupInline(chatId, BOOK_ANSWER_FOR_BUTTONS, true));
            return returnList;
            default:
                returnList.add(messageCreator.createMessageWithKeyboard(update.getMessage().getChatId(), ERROR_ANSWER, START_KEYBOARD));
            return returnList;
        }
    }

    public SendMessage startCommand(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привет, " + name + " , чем я могу тебе помочь?" + " :blush:");
        log.info("Replied to user:" + name);
        return messageCreator.createMessageWithKeyboard(chatId, answer, START_KEYBOARD);
    }

    public SendMessage help(long chatId, String name) {
        log.info("Replied to user:" + name);
        return messageCreator.createMessageWithKeyboard(chatId, HELP_ANSWER, START_KEYBOARD);
    }


}
