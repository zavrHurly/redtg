package com.example.red2.service.handlers;

import com.example.red2.repository.UserRepository;
import com.example.red2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Slf4j
public class StartBookingHandler implements Handler {

    private UserService service;

    public StartBookingHandler(UserRepository repository) {
        service = new UserService(repository);
    }

    @Override
    public boolean isProcessed(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public void processed(Update update) {
        if(!service.getAction(update.getMessage().getChatId())){
            service.setAction(update.getMessage().getChatId(), true);
        }
    }
}
