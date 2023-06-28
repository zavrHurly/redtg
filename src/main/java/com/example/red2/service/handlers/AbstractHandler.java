package com.example.red2.service.handlers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public abstract class AbstractHandler implements Handler {

    protected TelegramLongPollingBot executor;


    @Override
    public abstract boolean isProcessed(Update update);

    @Override
    public abstract void processed(Update update) throws TelegramApiException;
}
