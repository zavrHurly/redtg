package com.example.red2.service;

import com.example.red2.config.BotConfig;
import com.example.red2.service.creators.BookingCreator;
import com.example.red2.service.handlers.AbstractHandler;
import com.example.red2.service.handlers.AnswerHandler;
import com.example.red2.service.handlers.Handler;
import com.example.red2.service.handlers.RegisterHandler;
import jakarta.annotation.PostConstruct;
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

import java.text.ParseException;
import java.util.List;

import static com.example.red2.models.AnswersAndKeyboards.POSITIVE_BOOK_ANSWER_1;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    private final List<AbstractHandler> handlers;

    private final RegisterHandler register;


    @Autowired
    public TelegramBot(BotConfig botConfig, List<AbstractHandler> handlers, RegisterHandler register) {
        this.botConfig = botConfig;
        this.handlers = handlers;
        this.register = register;
    }

    @PostConstruct
    public void init(){
        for(Handler handler: handlers){
            handler.setExecutor(this);
        }
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
        if (register.isProcessed(update)) register.processed(update);
        for (Handler handler : handlers) {
            if (handler.isProcessed(update)) {
                try {
                    handler.processed(update);
                } catch (TelegramApiException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
