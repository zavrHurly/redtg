package com.example.red2.service;

import com.example.red2.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    private BotHelper helper;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        helper = new BotHelper();
        List<BotCommand> commands = createBotMenu(helper.createBotMenu());
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
        if(helper.checkingForThePresenceOfTimeInTheText(update)) {
            sendMessage(helper.putTheCoals(update));
        } else {
            sendMessage(helper.createAnswer(update));
        }
    }

    private List<BotCommand> createBotMenu(List<BotCommand> commands) {
        try{
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting command list: " + e);
        }
        return commands;
    }

    private void sendMessage(SendMessage message) {
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
