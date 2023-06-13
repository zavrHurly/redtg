package com.example.red2.service.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface  Handler {

    boolean isProcessed(Update update);

    List processed(Update update);
}
