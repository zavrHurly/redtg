package com.example.red2.service.handlers;

import com.example.red2.config.BotConfig;
import com.example.red2.service.TelegramBot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;

@Data
@Setter
@NoArgsConstructor
public abstract class AbstractHandler implements Handler {

    protected TelegramBot executor;



    @Override
    public abstract boolean isProcessed(Update update);

    @Override
    public abstract void processed(Update update) throws TelegramApiException, ParseException;
}
