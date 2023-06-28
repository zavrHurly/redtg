package com.example.red2.service.handlers;

import com.example.red2.config.BotConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
public abstract class AbstractHandler implements Handler {

    protected DefaultAbsSender executor;

    protected BotConfig config = new BotConfig();

    public AbstractHandler(DefaultBotOptions options){
        executor = new DefaultAbsSender(options) {
            @Override
            public String getBotToken() {
                return config.getToken();
            }
        };
    }


    @Override
    public abstract boolean isProcessed(Update update);

    @Override
    public abstract void processed(Update update) throws TelegramApiException;
}
