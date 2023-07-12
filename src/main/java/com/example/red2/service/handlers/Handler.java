package com.example.red2.service.handlers;

import com.example.red2.service.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.util.List;

public interface  Handler {

    boolean isProcessed(Update update);

    void processed(Update update) throws TelegramApiException, ParseException;

    void setExecutor(TelegramBot executor);
}
