package com.example.red2.service.handlers;

import com.example.red2.service.creators.MessageCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.red2.models.AnswersAndKeyboards.POSITIVE_RESULT_ANSWER;
import static com.example.red2.models.AnswersAndKeyboards.START_KEYBOARD;

@Component
@PropertySource("application.properties")
public class TheCoalsHandler extends AbstractHandler {

    protected MessageCreator messageCreator = new MessageCreator();

    @Value("${admin.chat}")
    private String adminChat;

    public TheCoalsHandler() {
        super(new DefaultBotOptions());
    }


    public boolean isProcessed(Update update) {
        if(update.hasMessage()) return update.getMessage().getText().contains("минут") || update.getMessage().getText().contains("час");
        return false;
    }

    public void processed(Update update) throws TelegramApiException {
        executor.execute(messageCreator.createMessageToAdmin(Long.parseLong(adminChat), update.getMessage().getText(), update.getMessage().getChat().getFirstName(), update.getMessage().getChat().getUserName()));
        executor.execute(messageCreator.createMessageWithKeyboard(update.getMessage().getChatId(), POSITIVE_RESULT_ANSWER, START_KEYBOARD));

    }
}
