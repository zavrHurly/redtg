package com.example.red2.service.handlers;

import com.example.red2.repository.UserRepository;
import com.example.red2.service.UserService;
import com.example.red2.service.creators.MessageCreator;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.red2.models.AnswersAndKeyboards.*;

@Component
@Slf4j

public class AnswerHandler extends AbstractHandler {

    private final MessageCreator messageCreator;

    private final UserService userService;

    @Autowired
    public AnswerHandler(UserRepository userRepository) {
        super(new DefaultBotOptions());
        this.userService = new UserService(userRepository);
        messageCreator = new MessageCreator();
    }

    public boolean isProcessed(Update update) {
        return update.hasMessage() & update.getMessage().hasText() &
                !(update.getMessage().getText().contains("минут") || update.getMessage().getText().contains("час")) &
                !userService.getAction(update.getMessage().getChatId());
    }

    public void processed(Update update) throws TelegramApiException {
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getChat().getFirstName();

        switch (message) {
            case "/start":
                executor.execute(startCommand(chatId, userName));
                break;
            case "/help":
                executor.execute(help(chatId, userName));
                break;
            case "Поставить угли":
                executor.execute(messageCreator.createMessageWithKeyboard(chatId, COAL_ANSWER, COAL_KEYBOARD));
                break;
            case "Забронировать стол":
                executor.execute(messageCreator.createMessageWithMarkupInline(chatId, BOOK_ANSWER_FOR_BUTTONS, true));
                break;
            default:
                executor.execute(messageCreator.createMessageWithKeyboard(update.getMessage().getChatId(), ERROR_ANSWER, START_KEYBOARD));
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
