package com.example.red2.service;


import com.example.red2.service.handlers.AnswerHandler;
import com.example.red2.service.handlers.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BotHelper {

    public List<BotCommand> createBotMenu() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "запуск"));
        commands.add(new BotCommand("/help", "описание"));
        return commands;
    }

    public Handler getHandler(Update update, List <Handler> handlers) {
        Handler handler = new AnswerHandler();
        for (Handler workHandler: handlers) {
            if(handler.isProcessed(update)){
                handler = workHandler;
            }
        }
        return handler;
    }



    public boolean textPresenceCheck(Update update) {
        return update.hasMessage() & update.getMessage().hasText();
    }

}
