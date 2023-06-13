package com.example.red2.service;

import com.example.red2.config.BotConfig;
import com.example.red2.service.handlers.AnswerHandler;
import com.example.red2.service.handlers.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.example.red2.models.AnswersAndKeyboards.POSITIVE_BOOK_ANSWER_1;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    private BotHelper helper;

    private final List<Handler> handlers;

    @Autowired
    public TelegramBot(BotConfig botConfig, BotHelper helper, List<Handler> handlers) {
        this.botConfig = botConfig;
        this.helper = helper;
        List<BotCommand> commands = createBotMenu(helper.createBotMenu());
        this.handlers = handlers;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    private List<BotCommand> createBotMenu(List<BotCommand> commands) {
        try{
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting command list: " + e);
        }
        return commands;
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMessage(EditMessageText message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMessage(List<SendMessage> messages) {
        try {
            for (SendMessage message : messages) {
                execute(message);
            }
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
